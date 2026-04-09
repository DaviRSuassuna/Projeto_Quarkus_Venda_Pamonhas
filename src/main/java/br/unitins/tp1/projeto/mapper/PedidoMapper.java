package br.unitins.tp1.projeto.mapper;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.dto.ItemPedidoRequestDTO;
import br.unitins.tp1.projeto.dto.ItemPedidoResponseDTO;
import br.unitins.tp1.projeto.model.Pedido;
import br.unitins.tp1.projeto.model.ItemPedido;
import br.unitins.tp1.projeto.model.StatusPedido;

public class PedidoMapper {

    public static Pedido toEntity(PedidoRequestDTO dto) {

        if (dto == null) return null;

        Pedido pedido = new Pedido();

        pedido.setStatus(StatusPedido.valueOf(dto.idStatusPedido()));
        pedido.setData(java.time.LocalDateTime.now());
        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        if (dto.itens() != null) {

            for (ItemPedidoRequestDTO itemDTO : dto.itens()) {

                ItemPedido item = new ItemPedido();

                item.setQuantidade(itemDTO.quantidade());
                item.setPedido(pedido);

                if (item.getPrecoUnitario() != null) {
                    BigDecimal subtotal = item.getPrecoUnitario()
                            .multiply(BigDecimal.valueOf(item.getQuantidade()));

                    total = total.add(subtotal);
                }

                itens.add(item);
            }
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        return pedido;
    }

    public static PedidoResponseDTO toResponseDTO(Pedido pedido) {

        if (pedido == null) return null;

        List<ItemPedidoResponseDTO> itensDTO = new ArrayList<>();

        if (pedido.getItens() != null) {

            for (ItemPedido item : pedido.getItens()) {

                BigDecimal subtotal = item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade()));

                itensDTO.add(new ItemPedidoResponseDTO(
                    item.getId(),
                    item.getPamonha().getNome(),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    subtotal
                ));
            }
        }

        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getCliente().getNome(),
            pedido.getStatus().getNOME(),
            pedido.getData(),
            pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO,
            pedido.getCupom() != null ? pedido.getCupom().getCodigo() : null,
            itensDTO
        );
    }
}