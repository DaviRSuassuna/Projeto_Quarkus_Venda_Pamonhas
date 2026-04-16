package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.ModoPreparo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModoPreparoRepository implements PanacheRepository<ModoPreparo> {

    public List<ModoPreparo> findByDescricao(String descricao) {
        return find("UPPER(descricao) LIKE UPPER(?1)", "%" + descricao + "%").list();
    }

    public List<ModoPreparo> findOrderByTempo() {
        return find("ORDER BY tempoPreparoMinutos ASC").list();
    }
}
