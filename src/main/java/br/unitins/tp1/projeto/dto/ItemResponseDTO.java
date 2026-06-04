package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

public record ItemResponseDTO(
    Long id,
    String nomePamonha,
    Integer quantidade,
    BigDecimal valorUnitario,
    BigDecimal descontoUnitario
) {}
