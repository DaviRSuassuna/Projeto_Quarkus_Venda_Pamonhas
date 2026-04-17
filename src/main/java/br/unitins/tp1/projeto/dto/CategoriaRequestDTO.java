package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
    @NotBlank String nome,
    @NotBlank String descricao
) {}
