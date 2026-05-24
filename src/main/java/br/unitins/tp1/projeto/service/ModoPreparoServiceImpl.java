package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.ModoPreparo;
import br.unitins.tp1.projeto.repository.ModoPreparoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ModoPreparoServiceImpl implements ModoPreparoService {

    @Inject
    ModoPreparoRepository repository;

    @Override
    public List<ModoPreparo> findAll(int page, int size) {
        return repository.findAll().page(page, size).list();
    }

    @Override
    public ModoPreparo findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<ModoPreparo> findByDescricao(String descricao) {
        return repository.findByDescricao(descricao);
    }

    @Override
    public List<ModoPreparo> findOrderByTempo() {
        return repository.findOrderByTempo();
    }

    @Override
    @Transactional
    public ModoPreparo create(ModoPreparo modoPreparo) {
        // Validação de negócio: verificar se a descrição já existe
        List<ModoPreparo> existentes = repository.findByDescricao(modoPreparo.getDescricao());
        if (!existentes.isEmpty()) {
            throw new ValidationException("descricao", "Já existe um modo de preparo com esta descrição");
        }
        
        repository.persist(modoPreparo);
        return modoPreparo;
    }

    @Override
    @Transactional
    public void update(Long id, ModoPreparoRequestDTO dto) {
        ModoPreparo modo = findById(id);
        if (modo == null) {
            throw new RuntimeException("ModoPreparo não encontrado: " + id);
        }
        
        // Validação de negócio: verificar se a descrição já existe (exceto para o próprio)
        List<ModoPreparo> existentes = repository.findByDescricao(dto.descricao());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(id)) {
            throw new ValidationException("descricao", "Já existe um modo de preparo com esta descrição");
        }
        
        modo.setDescricao(dto.descricao());
        modo.setTempoPreparoMinutos(dto.tempoPreparoMinutos());
        repository.persist(modo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
