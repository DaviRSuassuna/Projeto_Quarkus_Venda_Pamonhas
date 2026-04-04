package br.unitins.tp1.projeto.dto;

import br.unitins.tp1.projeto.model.UnidadeMedida;

public record ItemReceitaRequestDTO(
    Double quantidade,
    UnidadeMedida unidadeMedida,
    Long ingredienteId
) {}
