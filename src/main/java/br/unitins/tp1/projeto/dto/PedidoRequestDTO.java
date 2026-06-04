package br.unitins.tp1.projeto.dto;

import java.util.List;

import br.unitins.tp1.projeto.model.FormaPagamento;
import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(
    @NotNull Long enderecoId,
    Long cupomId,
    @NotNull FormaPagamento formaPagamento,
    @NotNull List<ItemRequestDTO> itens
) {}
