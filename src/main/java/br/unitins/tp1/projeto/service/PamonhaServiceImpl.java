package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
    public Pamonha findByIngredientePrincipal(String ingredientePrincipal) {
        return (Pamonha) repository.findByIngredientePrincipal(ingredientePrincipal);
    }

    @Override
    public List<Pamonha> findBySaborPamonha(String saborPamonha) {
        return (List<Pamonha>) repository.findBySaborPamonha(saborPamonha);
    }

    @Override
    public Pamonha create(Pamonha dto) {
        repository.persist(dto);
        return dto;
    }

    @Override
    public void update(Long id, PamonhaRequestDTO pamonha) {
        Pamonha pamonhaUpdate = findById(id);
        pamonhaUpdate.setIngredientePrincipal(pamonha.ingredientePrincipal());
        pamonhaUpdate.setPreco(pamonha.preco());
        pamonhaUpdate.setTemQueijo(pamonha.temQueijo());
        repository.persist(pamonhaUpdate);      
    }
    
    @Override
    public void delete(Long id) {
        repository.deleteById(id);       
    } 
}
