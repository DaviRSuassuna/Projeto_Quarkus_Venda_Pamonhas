package br.unitins.tp1.projeto.dto;

public record EnderecoDTO(
    String rua,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep
) {}
