package br.unitins.tp1.projeto.dto;

public record TabelaNutricionalRequestDTO(
    double valorEnergetico,
    double carboidratos,
    double proteinas,
    double gordurasTotais,
    double fibras,
    double sodio
) {}
