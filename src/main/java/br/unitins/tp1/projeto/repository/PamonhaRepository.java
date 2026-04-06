package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Pamonha;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PamonhaRepository implements PanacheRepository<Pamonha> {

    public List<Pamonha> findByTipoPamonha(String tipoPamonha) {
        return find("SELECT p FROM Pamonha p WHERE UPPER(p.tipo_pamonha) LIKE UPPER(?1)", "%"+tipoPamonha+"%").list();
    }

    public List<Pamonha> findBySaborPamonha(String saborPamonha) {
        return find("SELECT p FROM Pamonha p WHERE UPPER(p.sabor_pamonha) LIKE UPPER(?1)", "%"+saborPamonha+"%").list();
    }
}
