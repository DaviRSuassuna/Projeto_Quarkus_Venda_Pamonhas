package br.unitins.tp1.projeto.dto;

public record UsuarioResponseDTO(
    Long id,
    String login,
    String nome,
    String email
) {}
