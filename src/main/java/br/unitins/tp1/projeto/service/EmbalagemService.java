package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.model.Embalagem;

public interface EmbalagemService {

    List<Embalagem> findAll();

    Embalagem findById(Long id);

    List<Embalagem> findByTipo(String tipo);

    List<Embalagem> findOrderByCusto();

    List<Embalagem> findBiodegradavelRapida(int dias);

    Embalagem create(EmbalagemRequestDTO dto);

    void update(Long id, EmbalagemRequestDTO dto);

    void delete(Long id);
}
