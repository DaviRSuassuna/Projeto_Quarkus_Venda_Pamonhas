package br.unitins.tp1.projeto.dto;

public record EnderecoResponseDTO(
    Long id,
    String rua,
    String numero,
    String complemento,
    String bairro,
    String cep,
    String cidade,
    String estado
) {}
