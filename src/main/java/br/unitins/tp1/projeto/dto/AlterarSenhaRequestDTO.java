package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequestDTO(
    @NotBlank String senhaAtual,
    @NotBlank String novaSenha
) {}
