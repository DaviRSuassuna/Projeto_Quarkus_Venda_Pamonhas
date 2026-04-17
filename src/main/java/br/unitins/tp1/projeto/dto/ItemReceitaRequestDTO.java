package br.unitins.tp1.projeto.dto;

import br.unitins.tp1.projeto.model.UnidadeMedida;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemReceitaRequestDTO(
    @NotNull @Positive Double quantidade,
    @NotNull UnidadeMedida unidadeMedida,
    @NotNull @Positive Long ingredienteId
) {}
