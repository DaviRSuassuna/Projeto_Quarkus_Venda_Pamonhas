package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.EstadoRequestDTO;
import br.unitins.tp1.projeto.model.Estado;

public interface EstadoService {

    List<Estado> findAll();

    Estado findById(Long id);

    Estado create(EstadoRequestDTO dto);

    Estado update(Long id, EstadoRequestDTO dto);

    void delete(Long id);
}
