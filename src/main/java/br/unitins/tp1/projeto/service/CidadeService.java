package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CidadeRequestDTO;
import br.unitins.tp1.projeto.model.Cidade;

public interface CidadeService {

    List<Cidade> findAll();

    Cidade findById(Long id);

    List<Cidade> findByEstado(Long estadoId);

    Cidade create(CidadeRequestDTO dto);

    Cidade update(Long id, CidadeRequestDTO dto);

    void delete(Long id);
}
