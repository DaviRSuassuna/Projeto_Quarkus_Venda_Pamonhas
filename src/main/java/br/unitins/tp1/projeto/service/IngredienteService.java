package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.model.Ingrediente;

public interface IngredienteService {

    List<Ingrediente> findAll();

    Ingrediente findById(Long id);

    List<Ingrediente> findByUnidadeMedida(String unidadeMedida);

    List<Ingrediente> findByEstoqueAbaixo(Double qtdEstoque);

    Ingrediente create(Ingrediente dto);

    void update(Long id, IngredienteRequestDTO dto);

    void delete(Long id);
}
