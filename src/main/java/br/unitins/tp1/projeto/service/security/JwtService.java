package br.unitins.tp1.projeto.service.security;

import java.util.Set;

import br.unitins.tp1.projeto.model.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {

    private static final long EXPIRACAO_SEGUNDOS = 3600L; // 1 hora

    public String gerarToken(Usuario usuario) {
        return Jwt.issuer("venda-pamonhas-api")
                .upn(usuario.getLogin())
                .groups(Set.of(usuario.getPerfil().name()))
                .expiresIn(EXPIRACAO_SEGUNDOS)
                .sign();
    }
}
