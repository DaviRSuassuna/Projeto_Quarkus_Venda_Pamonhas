package br.unitins.tp1.projeto.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CadastroCompletoRequestDTO(
    @NotBlank String login,
    @NotBlank String senha,
    @NotBlank String nome,
    @NotBlank String sobrenome,
    @NotBlank String cpf,
    String email,
    String telefone,
    LocalDate dataNascimento
) {}
