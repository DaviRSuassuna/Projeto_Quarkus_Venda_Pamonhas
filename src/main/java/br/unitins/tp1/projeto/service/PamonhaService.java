package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.model.Pamonha;

public interface PamonhaService {

    List<Pamonha> findAll();

    Pamonha findById(Long id);

    List<Pamonha> findByTipoPamonha(String tipoPamonha);

    List<Pamonha> findBySaborPamonha(String saborPamonha);

    Pamonha create(Pamonha dto);

    void update(Long id, PamonhaRequestDTO dto);

    void delete(Long id);
}
