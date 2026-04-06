package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.model.CupomDesconto;
import br.unitins.tp1.projeto.repository.CupomDescontoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CupomDescontoServiceImpl implements CupomDescontoService{

    @Inject
    CupomDescontoRepository repository;

    @Override
    public List<CupomDesconto> findAll() {
        return repository.findAll().list();
    }

    @Override
    public CupomDesconto findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<CupomDesconto> findByAtivo() {
        return repository.findByAtivo();
    }

    @Override
    public List<CupomDesconto> listByValorDescontoCrescente() {
        return repository.listByValorDescontoCrescente();
    }

    @Override
    public List<CupomDesconto> listByValorDescontoDecrescente() {
        return repository.listByValorDescontoDecrescente();
    }

    @Override
    @Transactional
    public CupomDesconto create(CupomDesconto dto) {
        repository.persist(dto);
        return dto;
    }

    @Override
    @Transactional
    public void update(Long id, CupomDescontoRequestDTO dto) {
        CupomDesconto cupomDescontoUpdate = findById(id);
        cupomDescontoUpdate.setCodigo(dto.codigo());
        cupomDescontoUpdate.setValorDesconto(dto.valorDesconto());
        cupomDescontoUpdate.setDataValidade(dto.dataValidade());
        cupomDescontoUpdate.setAtivo(dto.ativo());
        repository.persist(cupomDescontoUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
