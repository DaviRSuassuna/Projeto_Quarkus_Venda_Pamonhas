package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.repository.CategoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CategoriaServiceImpl implements CategoriaService {

    @Inject
    CategoriaRepository repository;

    @Override
    public List<Categoria> findAll() {
        return repository.findAll().list();
    }

    @Override
    public Categoria findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Categoria> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public List<Categoria> findComPamonhas() {
        return repository.findComPamonhas();
    }

    @Override
    @Transactional
    public Categoria create(Categoria categoria) {
        // Validação de negócio: verificar se o nome já existe
        List<Categoria> existentes = repository.findByNome(categoria.getNome());
        if (!existentes.isEmpty()) {
            throw new ValidationException("nome", "Já existe uma categoria com este nome");
        }
        
        repository.persist(categoria);
        return categoria;
    }

    @Override
    @Transactional
    public void update(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = findById(id);
        if (categoria == null) {
            throw new RuntimeException("Categoria não encontrada: " + id);
        }
        
        // Validação de negócio: verificar se o nome já existe (exceto para o próprio)
        List<Categoria> existentes = repository.findByNome(dto.nome());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(id)) {
            throw new ValidationException("nome", "Já existe uma categoria com este nome");
        }
        
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        repository.persist(categoria);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
