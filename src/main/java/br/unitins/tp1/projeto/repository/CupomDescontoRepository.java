package br.unitins.tp1.projeto.repository;

import br.unitins.tp1.projeto.model.CupomDesconto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CupomDescontoRepository implements PanacheRepository<CupomDesconto> {

    public CupomDesconto findByCodigo(String codigo) {
        return find("codigo", codigo).firstResult();
    }
}
