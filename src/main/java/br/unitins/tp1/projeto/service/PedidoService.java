package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.model.StatusPedido;

public interface PedidoService {

    PedidoResponseDTO criar(String login, PedidoRequestDTO dto);

    List<PedidoResponseDTO> listarPorUsuario(String login);

    PedidoResponseDTO buscarPorId(String login, Long id);

    void atualizarStatus(Long id, StatusPedido novoStatus);
}
