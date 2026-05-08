package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmbalagemRepository implements PanacheRepository<Embalagem> {

    public List<Embalagem> findByTipo(String tipo) {
        if ("BIODEGRADAVEL".equalsIgnoreCase(tipo)) {
            return find("FROM Embalagem e WHERE TYPE(e) = ?1", EmbalagemBiodegradavel.class).list();
        }
        if ("PLASTICA".equalsIgnoreCase(tipo)) {
            return find("FROM Embalagem e WHERE TYPE(e) = ?1", EmbalagemPlastica.class).list();
        }
        return listAll();
    }

    public List<Embalagem> findOrderByCusto() {
        return find("ORDER BY custo ASC").list();
    }

    public List<Embalagem> findBiodegradavelRapida(int dias) {
        return find("FROM Embalagem e WHERE TYPE(e) = EmbalagemBiodegradavel AND tempoDecomposicaoDias <= ?1", dias).list();
    }

    public List<Embalagem> findByDescricao(String descricao) {
        return find("descricao = ?1", descricao).list();
    }
}
