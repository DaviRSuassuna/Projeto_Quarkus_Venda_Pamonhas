package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.UnidadeMedida;
import br.unitins.tp1.projeto.repository.IngredienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IngredienteServiceImpl implements IngredienteService {

    @Inject
    IngredienteRepository repository;

    
    @Override
    public List<Ingrediente> findAll() {
        return repository.findAll().list();
    }

    @Override
    public Ingrediente findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Ingrediente> findByEstoqueAbaixo(double qtdEstoque) {
        return repository.findByEstoqueAbaixo(qtdEstoque);
    }

    @Override
    public List<Ingrediente> findByUnidadeMedida(String unidadeMedida) {
        return repository.findByUnidadeMedida(unidadeMedida);
    }

    @Override
    public Ingrediente create(Ingrediente dto) {
        repository.persist(dto);
        return dto;
    }

    @Override
    public void update(Long id, IngredienteRequestDTO dto) {
        Ingrediente ingredienteUpdate = findById(id);
        ingredienteUpdate.setNome(dto.nome());
        ingredienteUpdate.setPrecoUnitario(dto.precoUnitario());
        ingredienteUpdate.setEstoque(dto.estoque());
        ingredienteUpdate.setUnidadeMedida(UnidadeMedida.valueOf(dto.idUnidadeMedida()));
        repository.persist(ingredienteUpdate);
        
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
        
    }
 
}