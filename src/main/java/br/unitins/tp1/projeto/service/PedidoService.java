package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.model.Pedido;
import br.unitins.tp1.projeto.model.StatusPedido;

public interface PedidoService {

    List<Pedido> findAll();

    Pedido findById(Long id);

    List<Pedido> findByClienteNome(String nome);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByClienteId(Long idCliente);

    List<Pedido> findByData(String data);

    PedidoResponseDTO create(PedidoRequestDTO dto);

    void update(Long id, PedidoRequestDTO dto);

    void delete(Long id);
}