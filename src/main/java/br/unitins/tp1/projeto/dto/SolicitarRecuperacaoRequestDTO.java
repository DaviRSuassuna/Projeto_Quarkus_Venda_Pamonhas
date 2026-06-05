package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitarRecuperacaoRequestDTO(
    @Email @NotBlank String email
) {}
