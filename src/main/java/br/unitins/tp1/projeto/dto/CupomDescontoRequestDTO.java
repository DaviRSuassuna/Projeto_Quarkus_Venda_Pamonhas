package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CupomDescontoRequestDTO(
    String codigo,
    BigDecimal valorDesconto,
    LocalDate dataValidade,
    Boolean ativo
) {}
