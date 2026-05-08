package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record IngredienteRequestDTO(
    @NotBlank String nome, 
    @NotNull @Positive BigDecimal precoUnitario, 
    @PositiveOrZero double estoque, 
    @NotNull Long idUnidadeMedida
) {}