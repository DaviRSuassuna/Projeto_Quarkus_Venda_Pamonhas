package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CupomDescontoRequestDTO(
    @NotBlank String codigo,
    @NotBlank String descricao,
    @NotNull BigDecimal valorDesconto,
    @NotNull LocalDate dataValidade,
    List<Long> pamonhaIds
) {}
