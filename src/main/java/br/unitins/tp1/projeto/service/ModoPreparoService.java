package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.model.ModoPreparo;

public interface ModoPreparoService {

    List<ModoPreparo> findAll();

    ModoPreparo findById(Long id);

    List<ModoPreparo> findByDescricao(String descricao);

    List<ModoPreparo> findOrderByTempo();

    ModoPreparo create(ModoPreparo modoPreparo);

    void update(Long id, ModoPreparoRequestDTO dto);

    void delete(Long id);
}
