package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRequestDTO(
    @NotBlank String token,
    @NotBlank String novaSenha
) {}
