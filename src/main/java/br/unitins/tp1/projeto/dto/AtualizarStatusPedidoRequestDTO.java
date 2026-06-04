package br.unitins.tp1.projeto.dto;

import br.unitins.tp1.projeto.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoRequestDTO(
    @NotNull StatusPedido status
) {}
