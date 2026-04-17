package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record TabelaNutricionalRequestDTO(
    @PositiveOrZero double valorEnergetico,
    @PositiveOrZero double carboidratos,
    @PositiveOrZero double proteinas,
    @PositiveOrZero double gordurasTotais,
    @PositiveOrZero double fibras,
    @PositiveOrZero double sodio
) {}
