package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.model.Categoria;

public interface CategoriaService {

    List<Categoria> findAll(int page, int size);

    Categoria findById(Long id);

    List<Categoria> findByNome(String nome);

    List<Categoria> findComPamonhas();

    Categoria create(Categoria categoria);

    void update(Long id, CategoriaRequestDTO dto);

    void delete(Long id);
}
