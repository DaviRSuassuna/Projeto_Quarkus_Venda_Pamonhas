package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Pamonha;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PamonhaRepository implements PanacheRepository<Pamonha> {

    public List<Pamonha> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%").list();
    }

    public List<Pamonha> findByCategoria(Long categoriaId) {
        return find("SELECT p FROM Pamonha p JOIN p.categorias c WHERE c.id = ?1", categoriaId).list();
    }
}

