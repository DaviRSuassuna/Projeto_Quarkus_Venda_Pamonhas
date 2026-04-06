package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.CupomDesconto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CupomDescontoRepository implements PanacheRepository<CupomDesconto>{

    public List<CupomDesconto> findByAtivo() {
        return find("SELECT cd FROM CupomDesconto cd WHERE cd.ativo = true").list();
    }

    public List<CupomDesconto> listByValorDescontoCrescente() {
        return findAll(Sort.by("valorDesconto")).list(); //Usa o nome do atributo e não o da coluna no banco
    }

    public List<CupomDesconto> listByValorDescontoDecrescente() {
        return findAll(Sort.by("valorDesconto").descending()).list(); //Usa o nome do atributo e não o da coluna no banco
    }
}
