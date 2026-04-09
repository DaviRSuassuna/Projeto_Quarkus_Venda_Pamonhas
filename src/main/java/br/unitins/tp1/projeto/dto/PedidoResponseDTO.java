package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    String nomeCliente,
    String status,
    LocalDateTime data,
    BigDecimal total,
    String codigoCupom,
    List<ItemPedidoResponseDTO> itens
) {}