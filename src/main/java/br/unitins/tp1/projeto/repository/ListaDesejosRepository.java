package br.unitins.tp1.projeto.repository;

import br.unitins.tp1.projeto.model.ListaDesejos;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ListaDesejosRepository implements PanacheRepository<ListaDesejos> {

    public ListaDesejos findByUsuario(String login) {
        return find("usuario.login", login).firstResult();
    }
}
