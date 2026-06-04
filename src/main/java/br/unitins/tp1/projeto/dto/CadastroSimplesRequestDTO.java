package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastroSimplesRequestDTO(
    @NotBlank String login,
    @NotBlank String senha
) {}
