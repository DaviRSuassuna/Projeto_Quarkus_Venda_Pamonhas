package br.unitins.tp1.projeto.dto;

public record EditarUsuarioRequestDTO(
    String nome,
    String email,
    String telefone
) {}
