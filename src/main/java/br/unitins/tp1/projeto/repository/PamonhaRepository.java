package br.unitins.tp1.projeto.repository;

import br.unitins.tp1.projeto.model.Pamonha;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PamonhaRepository implements PanacheRepository<Pamonha> {

    public PanacheQuery<Pamonha> findByIngredientePrincipal(String ingredientePrincipal) {
        return find("SELECT p FROM Pamonha p WHERE UPPER(p.ingrediente_principal) LIKE UPPER(?1)", "%"+ingredientePrincipal+"%");
    }

    public PanacheQuery<Pamonha> findBySaborPamonha(String saborPamonha) {
        return find("SELECT p FROM Pamonha p WHERE UPPER(p.sabor_pamonha) LIKE UPPER(?1)", "%"+saborPamonha+"%");
    }
}
