package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.SaborPamonha;
import br.unitins.tp1.projeto.model.TipoPamonha;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PamonhaServiceImpl implements PamonhaService{

    @Inject
    PamonhaRepository repository;

    @Override
    public List<Pamonha> findAll() {
        return repository.findAll().list();
    }

    @Override
    public Pamonha findById(Long id) {
        return repository.findById(id);
    }
    
    @Override
    public List<Pamonha> findBySaborPamonha(String saborPamonha) {
        return repository.findBySaborPamonha(saborPamonha);
    }

    @Override
    public List<Pamonha> findByTipoPamonha(String tipoPamonha) {
        return repository.findByTipoPamonha(tipoPamonha);
    }

    @Override
    @Transactional
    public Pamonha create(Pamonha dto) {
        repository.persist(dto);
        return dto;
    }

    @Override
    @Transactional
    public void update(Long id, PamonhaRequestDTO dto) {
        Pamonha pamonhaUpdate = findById(id);
        pamonhaUpdate.setNome(dto.nome());
        pamonhaUpdate.setDescricao(dto.descricao());
        pamonhaUpdate.setPreco(dto.preco());
        pamonhaUpdate.setEstoque(dto.estoque());
        pamonhaUpdate.setSaborPamonha(SaborPamonha.valueOf(dto.idSaborPamonha()));
        pamonhaUpdate.setTipoPamonha(TipoPamonha.valueOf(dto.idTipoPamonha()));
        repository.persist(pamonhaUpdate);      
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);       
    } 
}
