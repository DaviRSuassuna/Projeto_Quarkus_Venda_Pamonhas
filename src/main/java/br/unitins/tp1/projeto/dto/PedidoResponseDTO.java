package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    LocalDateTime dataCriacao,
    BigDecimal total,
    BigDecimal desconto,
    String status,
    List<ItemResponseDTO> itens,
    String formaPagamento,
    String statusPagamento
) {}
