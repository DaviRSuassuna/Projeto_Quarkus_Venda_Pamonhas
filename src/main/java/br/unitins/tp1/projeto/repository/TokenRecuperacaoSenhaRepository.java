package br.unitins.tp1.projeto.repository;

import br.unitins.tp1.projeto.model.TokenRecuperacaoSenha;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenRecuperacaoSenhaRepository implements PanacheRepository<TokenRecuperacaoSenha> {

    public TokenRecuperacaoSenha findByToken(String token) {
        return find("token", token).firstResult();
    }
}
