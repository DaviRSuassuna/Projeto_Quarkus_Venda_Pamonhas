package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Pamonha;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PamonhaRepository implements PanacheRepository<Pamonha> {

    public List<Pamonha> findByIngredientePrincipal(String ingredientePrincipal) {
        return find("SELECT p FROM Pamonha p WHERE UPPER(p.ingredientePrincipal) LIKE UPPER(?1)", "%"+ingredientePrincipal+"%").list();
    }

    public List<Pamonha> findBySaborPamonha(String saborPamonha) {
        return find("SELECT p FROM Pamonha p WHERE UPPER(p.sabor_pamonha) LIKE UPPER(?1)", "%"+saborPamonha+"%").list();
    }
}
