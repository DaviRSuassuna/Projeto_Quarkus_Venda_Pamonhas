package br.unitins.tp1.projeto.dto;

public record EditarUsuarioRequestDTO(
    String nome,
    String sobrenome,
    String email,
    String telefone,
    String cpf,
    java.time.LocalDate dataNascimento
) {}
