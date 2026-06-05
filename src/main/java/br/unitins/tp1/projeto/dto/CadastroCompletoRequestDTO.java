package br.unitins.tp1.projeto.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastroCompletoRequestDTO(
    @Email @NotBlank String email,
    @NotBlank String senha,
    @NotBlank String nome,
    @NotBlank String sobrenome,
    @NotBlank String cpf,
    String telefone,
    LocalDate dataNascimento
) {}
