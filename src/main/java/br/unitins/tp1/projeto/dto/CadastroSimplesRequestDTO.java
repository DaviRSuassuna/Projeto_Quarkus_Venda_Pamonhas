package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastroSimplesRequestDTO(
    @Email @NotBlank String email,
    @NotBlank String senha
) {}
