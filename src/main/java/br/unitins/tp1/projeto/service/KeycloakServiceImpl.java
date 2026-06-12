package br.unitins.tp1.projeto.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakServiceImpl implements KeycloakService {

    private static final Logger LOG = Logger.getLogger(KeycloakServiceImpl.class);

    @ConfigProperty(name = "keycloak.admin.base-url", defaultValue = "http://localhost:8180")
    String baseUrl;

    @ConfigProperty(name = "keycloak.admin.realm", defaultValue = "venda-pamonhas")
    String realm;

    @ConfigProperty(name = "keycloak.admin.client-id", defaultValue = "admin-cli")
    String adminClientId;

    @ConfigProperty(name = "keycloak.admin.username", defaultValue = "admin")
    String adminUsername;

    @ConfigProperty(name = "keycloak.admin.password", defaultValue = "admin")
    String adminPassword;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String obterAdminToken() throws Exception {
        String url = baseUrl + "/realms/master/protocol/openid-connect/token";

        String body = "grant_type=password"
                + "&client_id=" + URLEncoder.encode(adminClientId, StandardCharsets.UTF_8)
                + "&username=" + URLEncoder.encode(adminUsername, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(adminPassword, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao obter token admin. Status: " + response.statusCode());
        }

        var json = objectMapper.readTree(response.body());
        return json.get("access_token").asText();
    }

    @Override
    public String registrarUsuario(String login, String senha, String role) {
        try {
            String adminToken = obterAdminToken();
            String url = baseUrl + "/admin/realms/" + realm + "/users";

            Map<String, Object> payload = Map.of(
                "username", login,
                "email", login,
                "enabled", true,
                "emailVerified", true,
                "credentials", List.of(Map.of(
                    "type", "password",
                    "value", senha,
                    "temporary", false
                ))
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + adminToken)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                String location = response.headers().firstValue("Location").orElse(null);
                if (location != null) {
                    String keycloakId = location.substring(location.lastIndexOf("/") + 1);
                    LOG.infof("Usuário '%s' registrado no Keycloak com id: %s", login, keycloakId);
                    atribuirRole(adminToken, keycloakId, role);
                    return keycloakId;
                }
            } else {
                LOG.warnf("Falha ao registrar usuário '%s' no Keycloak. Status: %d, Body: %s",
                        login, response.statusCode(), response.body());
            }
            return null;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao registrar usuário '%s' no Keycloak", login);
            return null;
        }
    }

    private void atribuirRole(String adminToken, String keycloakId, String roleName) {
        try {
            String roleUrl = baseUrl + "/admin/realms/" + realm + "/roles/" + roleName;
            HttpRequest roleRequest = HttpRequest.newBuilder()
                    .uri(URI.create(roleUrl))
                    .header("Authorization", "Bearer " + adminToken)
                    .GET()
                    .build();

            HttpResponse<String> roleResponse = httpClient.send(roleRequest, HttpResponse.BodyHandlers.ofString());

            if (roleResponse.statusCode() != 200) {
                LOG.warnf("Role '%s' não encontrada no Keycloak. Crie-a no Admin Console.", roleName);
                return;
            }

            String assignUrl = baseUrl + "/admin/realms/" + realm + "/users/" + keycloakId + "/role-mappings/realm";
            HttpRequest assignRequest = HttpRequest.newBuilder()
                    .uri(URI.create(assignUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + adminToken)
                    .POST(HttpRequest.BodyPublishers.ofString("[" + roleResponse.body() + "]"))
                    .build();

            httpClient.send(assignRequest, HttpResponse.BodyHandlers.ofString());
            LOG.infof("Role '%s' atribuída ao usuário Keycloak '%s'", roleName, keycloakId);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atribuir role '%s' ao usuário Keycloak '%s'", roleName, keycloakId);
        }
    }

    @Override
    public void deletarUsuario(String keycloakId) {
        try {
            String adminToken = obterAdminToken();
            String url = baseUrl + "/admin/realms/" + realm + "/users/" + keycloakId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + adminToken)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                LOG.infof("Usuário removido do Keycloak. id: %s", keycloakId);
            } else {
                LOG.warnf("Falha ao remover usuário do Keycloak. Status: %d", response.statusCode());
            }
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao remover usuário do Keycloak. id: %s", keycloakId);
        }
    }

    @Override
    public void atualizarEmail(String keycloakId, String novoEmail) {
        try {
            String adminToken = obterAdminToken();
            String url = baseUrl + "/admin/realms/" + realm + "/users/" + keycloakId;

            Map<String, Object> payload = Map.of("username", novoEmail, "email", novoEmail);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + adminToken)
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                LOG.infof("E-mail atualizado no Keycloak para usuário id: %s", keycloakId);
            } else {
                LOG.warnf("Falha ao atualizar e-mail no Keycloak. Status: %d, Body: %s",
                        response.statusCode(), response.body());
            }
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar e-mail no Keycloak para usuário id: %s", keycloakId);
        }
    }

    @Override
    public void atualizarRoles(String keycloakId, List<String> roles) {
        try {
            String adminToken = obterAdminToken();

            // Busca roles atuais
            String mappingsUrl = baseUrl + "/admin/realms/" + realm + "/users/" + keycloakId + "/role-mappings/realm";
            HttpRequest getRolesRequest = HttpRequest.newBuilder()
                    .uri(URI.create(mappingsUrl))
                    .header("Authorization", "Bearer " + adminToken)
                    .GET()
                    .build();

            HttpResponse<String> getRolesResponse = httpClient.send(getRolesRequest, HttpResponse.BodyHandlers.ofString());

            // Remove roles ROLE_* atuais
            if (getRolesResponse.statusCode() == 200) {
                JsonNode rolesArray = objectMapper.readTree(getRolesResponse.body());
                List<JsonNode> rolesToRemove = new ArrayList<>();
                for (JsonNode roleNode : rolesArray) {
                    String roleName = roleNode.get("name").asText();
                    if (roleName.startsWith("ROLE_")) {
                        rolesToRemove.add(roleNode);
                    }
                }
                if (!rolesToRemove.isEmpty()) {
                    String removeBody = objectMapper.writeValueAsString(rolesToRemove);
                    HttpRequest deleteRequest = HttpRequest.newBuilder()
                            .uri(URI.create(mappingsUrl))
                            .header("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + adminToken)
                            .method("DELETE", HttpRequest.BodyPublishers.ofString(removeBody))
                            .build();
                    httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
                    LOG.infof("Roles removidas do usuário Keycloak '%s'", keycloakId);
                }
            }

            // Atribui novas roles
            for (String roleName : roles) {
                atribuirRole(adminToken, keycloakId, roleName);
            }

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar roles do usuário Keycloak '%s'", keycloakId);
        }
    }

}