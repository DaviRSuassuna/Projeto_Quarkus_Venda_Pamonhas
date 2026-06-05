package br.unitins.tp1.projeto.repository;

import br.unitins.tp1.projeto.model.PessoaFisica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PessoaFisicaRepository implements PanacheRepository<PessoaFisica> {

    public PessoaFisica findByUsuarioEmail(String email) {
        return find("usuario.email", email).firstResult();
    }

    public PessoaFisica findByEmail(String email) {
        return find("email", email).firstResult();
    }
}
