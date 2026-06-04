package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record SolicitarRecuperacaoRequestDTO(
    @NotBlank String login
) {}
