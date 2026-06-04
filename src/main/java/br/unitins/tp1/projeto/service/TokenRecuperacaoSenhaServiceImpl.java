package br.unitins.tp1.projeto.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import br.unitins.tp1.projeto.dto.RedefinirSenhaRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.TokenRecuperacaoSenha;
import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.TokenRecuperacaoSenhaRepository;
import br.unitins.tp1.projeto.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TokenRecuperacaoSenhaServiceImpl implements TokenRecuperacaoSenhaService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    TokenRecuperacaoSenhaRepository tokenRepository;

    @Override
    @Transactional
    public String solicitarRecuperacao(String login) {
        Usuario usuario = usuarioRepository.find("login", login).firstResult();
        if (usuario == null) return null;

        String tokenStr = UUID.randomUUID().toString();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
        token.setToken(tokenStr);
        token.setDataExpiracao(LocalDateTime.now().plusHours(2));
        token.setUsado(false);
        token.setUsuario(usuario);
        tokenRepository.persist(token);
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
    }
}
