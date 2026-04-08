package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
    Long id,
    String nomePamonha,
    int quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal
) {}