package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ModoPreparoRequestDTO(
    @NotBlank String descricao,
    @Positive int tempoPreparoMinutos
) {}
