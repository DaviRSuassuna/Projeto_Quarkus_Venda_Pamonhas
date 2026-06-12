package br.unitins.tp1.projeto.service;

import java.util.List;

public interface KeycloakService {

    String registrarUsuario(String login, String senha, String role);

    void deletarUsuario(String keycloakId);

    void atualizarEmail(String keycloakId, String novoEmail);

    void atualizarRoles(String keycloakId, List<String> roles);
}
