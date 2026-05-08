package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmbalagemRequestDTO(
    @NotBlank String tipo,
    @NotBlank String descricao,
    @NotNull @Positive BigDecimal custo,
    @Positive double pesoSuportado,
    String tipoPlastico,
    Boolean reciclavel,
    String material,
    Integer tempoDecomposicaoDias
) {}