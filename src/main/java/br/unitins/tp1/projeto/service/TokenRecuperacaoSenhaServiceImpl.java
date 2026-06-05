package br.unitins.tp1.projeto.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import br.unitins.tp1.projeto.dto.RedefinirSenhaRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.PessoaFisica;
import br.unitins.tp1.projeto.model.TokenRecuperacaoSenha;
import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.PessoaFisicaRepository;
import br.unitins.tp1.projeto.repository.TokenRecuperacaoSenhaRepository;
import br.unitins.tp1.projeto.repository.UsuarioRepository;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TokenRecuperacaoSenhaServiceImpl implements TokenRecuperacaoSenhaService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    TokenRecuperacaoSenhaRepository tokenRepository;

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Inject
    Mailer mailer;

    @Inject
    KeycloakService keycloakService;

    @Override
    @Transactional
    public String solicitarRecuperacao(String email) {
        PessoaFisica pessoaFisica = pessoaFisicaRepository.findByEmail(email);
        if (pessoaFisica == null) return null;
        Usuario usuario = pessoaFisica.getUsuario();

        String tokenStr = UUID.randomUUID().toString();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
        token.setToken(tokenStr);
        token.setDataExpiracao(LocalDateTime.now().plusHours(2));
        token.setUsado(false);
        token.setUsuario(usuario);
        tokenRepository.persist(token);

        if (pessoaFisica.getEmail() != null) {
            mailer.send(Mail.withText(
                pessoaFisica.getEmail(),
                "Recuperação de Senha - Venda de Pamonhas",
                "Olá, " + pessoaFisica.getNome() + "!\n\n" +
                "Seu código para redefinir a senha é:\n\n" +
                tokenStr +
                "\n\nEste código expira em 2 horas.\n\n" +
                "Se não foi você quem solicitou, ignore este email."
            ));
        }

        return tokenStr;
    }

    @Override
    @Transactional
    public void redefinirSenha(RedefinirSenhaRequestDTO dto) {
        TokenRecuperacaoSenha token = tokenRepository.findByToken(dto.token());
        if (token == null || token.isUsado() || token.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new ValidationException("token", "Token inválido ou expirado");
        }
        Usuario usuario = token.getUsuario();
        usuario.setSenha(BCrypt.hashpw(dto.novaSenha(), BCrypt.gensalt()));
        usuarioRepository.persist(usuario);
        token.setUsado(true);
        tokenRepository.persist(token);

        try {
            if (usuario.getKeycloakId() != null) {
                keycloakService.atualizarSenha(usuario.getKeycloakId(), dto.novaSenha());
            }
        } catch (Exception e) {
            Log.warnf("Não foi possível atualizar senha no Keycloak para '%s': %s", usuario.getEmail(), e.getMessage());
        }
    }
}
