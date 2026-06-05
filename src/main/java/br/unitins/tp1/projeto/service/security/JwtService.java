package br.unitins.tp1.projeto.service.security;

import java.util.Set;
import java.util.stream.Collectors;

import br.unitins.tp1.projeto.model.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {

    private static final long EXPIRACAO_SEGUNDOS = 3600L; // 1 hora

    public String gerarToken(Usuario usuario) {
        Set<String> grupos = usuario.getPerfis().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        return Jwt.issuer("venda-pamonhas-api")
                .upn(usuario.getEmail())
                .groups(grupos)
                .expiresIn(EXPIRACAO_SEGUNDOS)
                .sign();
    }
}
