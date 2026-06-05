package br.unitins.tp1.projeto.service;

import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.UsuarioRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StartupService {

    private static final Logger LOG = Logger.getLogger(StartupService.class);

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    KeycloakService keycloakService;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            Usuario admin = usuarioRepository.find("email", "admin@pamonhas.com").firstResult();
            if (admin != null && admin.getKeycloakId() == null) {
                String keycloakId = keycloakService.registrarUsuario("admin@pamonhas.com", "admin", "ROLE_ADMIN");
                if (keycloakId != null) {
                    admin.setKeycloakId(keycloakId);
                    LOG.info("Admin registrado no Keycloak com sucesso.");
                }
            }
        } catch (Exception e) {
            LOG.warnf("Não foi possível registrar o admin no Keycloak no startup: %s", e.getMessage());
        }
    }
}
