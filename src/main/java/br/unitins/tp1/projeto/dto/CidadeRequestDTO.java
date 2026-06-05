package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CidadeRequestDTO(
    @NotBlank String nome,
    @NotNull Long estadoId
) {}
