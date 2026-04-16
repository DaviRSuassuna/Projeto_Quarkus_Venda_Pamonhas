package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Categoria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaRepository implements PanacheRepository<Categoria> {

    public List<Categoria> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%").list();
    }

    public List<Categoria> findComPamonhas() {
        return find("SELECT DISTINCT c FROM Categoria c JOIN c.pamonhas p").list();
    }
}
