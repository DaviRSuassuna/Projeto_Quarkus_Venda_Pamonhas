package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

public record IngredienteRequestDTO(
    String nome, 
    BigDecimal precoUnitario, 
    double estoque, 
    Long idUnidadeMedida
) {}