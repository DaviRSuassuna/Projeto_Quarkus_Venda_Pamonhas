package br.unitins.tp1.projeto.dto;

import br.unitins.tp1.projeto.model.UnidadeMedida;

public record ItemReceitaResponseDTO(
    double quantidade,
    UnidadeMedida unidadeMedida,
    String nomeIngrediente
) {}
