package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CupomDescontoResponseDTO(
    Long id,
    String codigo,
    BigDecimal valorDesconto,
    LocalDate dataValidade,
    Boolean ativo
) {}
