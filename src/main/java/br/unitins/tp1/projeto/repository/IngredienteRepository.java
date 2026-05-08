package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Ingrediente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IngredienteRepository implements PanacheRepository<Ingrediente> {

    public List<Ingrediente> findByUnidadeMedida(String unidadeMedida) {
        return find("SELECT i FROM Ingrediente i WHERE UPPER(i.unidadeMedida) LIKE UPPER(?1)", "%"+unidadeMedida+"%").list();
    }

    public List<Ingrediente> findByEstoqueAbaixo(Double qtdEstoque) {
        return find("estoque < ?1", qtdEstoque).list();
    }

    public List<Ingrediente> findByNome(String nome) {
        return find("nome = ?1", nome).list();
    }
}
