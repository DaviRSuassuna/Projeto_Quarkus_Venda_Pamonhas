package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

import br.unitins.tp1.projeto.model.UnidadeMedida;

public record IngredienteResponseDTO(
    Long id, 
    String nome, 
    BigDecimal precoUnitario, 
    double estoque, 
    UnidadeMedida unidadeMedida
) {}
