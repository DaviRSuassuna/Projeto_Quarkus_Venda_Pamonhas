package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoRequestDTO(
    @NotBlank String nome,
    @NotBlank String sigla
) {}
