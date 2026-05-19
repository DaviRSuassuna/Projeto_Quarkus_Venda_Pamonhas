package br.unitins.tp1.projeto.dto;

public record ModoPreparoResponseDTO(
    Long id,
    String descricao,
    int tempoPreparoMinutos
) {}
