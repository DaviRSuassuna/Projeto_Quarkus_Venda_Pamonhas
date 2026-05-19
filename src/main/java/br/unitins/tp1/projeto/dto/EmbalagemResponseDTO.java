package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

public record EmbalagemResponseDTO(
    Long id,
    String tipo,
    String descricao,
    BigDecimal custo,
    double pesoSuportado,
    String tipoPlastico,
    Boolean reciclavel,
    String material,
    Integer tempoDecomposicaoDias
) {}
