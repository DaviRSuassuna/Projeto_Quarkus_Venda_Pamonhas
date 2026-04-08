package br.unitins.tp1.projeto.dto;

import java.util.List;

public record PedidoRequestDTO(
    Long idCliente,
    Long idStatusPedido,
    List<ItemPedidoRequestDTO> itens
) {}