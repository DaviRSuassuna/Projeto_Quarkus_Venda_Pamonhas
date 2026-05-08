package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.UnidadeMedida;
import br.unitins.tp1.projeto.repository.IngredienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
    public List<Ingrediente> findByEstoqueAbaixo(Double qtdEstoque) {
        return repository.findByEstoqueAbaixo(qtdEstoque);
    }

    @Override
    public List<Ingrediente> findByUnidadeMedida(String unidadeMedida) {
        return repository.findByUnidadeMedida(unidadeMedida);
    }

    @Override
    @Transactional
    public Ingrediente create(Ingrediente dto) {
        // Validação de negócio: verificar se o nome já existe
        List<Ingrediente> existentes = repository.findByNome(dto.getNome());
        if (!existentes.isEmpty()) {
            throw new ValidationException("nome", "Já existe um ingrediente com este nome");
        }
        
        repository.persist(dto);
        return dto;
    }

    @Override
    @Transactional
    public void update(Long id, IngredienteRequestDTO dto) {
        Ingrediente ingredienteUpdate = findById(id);
        
        // Validação de negócio: verificar se o nome já existe (exceto para o próprio)
        List<Ingrediente> existentes = repository.findByNome(dto.nome());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(id)) {
            throw new ValidationException("nome", "Já existe um ingrediente com este nome");
        }
        
        ingredienteUpdate.setNome(dto.nome());
        ingredienteUpdate.setPrecoUnitario(dto.precoUnitario());
        ingredienteUpdate.setEstoque(dto.estoque());
        ingredienteUpdate.setUnidadeMedida(UnidadeMedida.valueOf(dto.idUnidadeMedida()));
        repository.persist(ingredienteUpdate);
        
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
        
    }
 
}