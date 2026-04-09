package br.unitins.tp1.projeto.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.dto.ItemPedidoRequestDTO;
import br.unitins.tp1.projeto.mapper.PedidoMapper;
import br.unitins.tp1.projeto.model.Pedido;
import br.unitins.tp1.projeto.model.ItemPedido;
import br.unitins.tp1.projeto.model.Cliente;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.StatusPedido;
import br.unitins.tp1.projeto.model.CupomDesconto;
import br.unitins.tp1.projeto.repository.PedidoRepository;
import br.unitins.tp1.projeto.repository.ClienteRepository;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import br.unitins.tp1.projeto.repository.CupomDescontoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    PamonhaRepository pamonhaRepository;

    @Inject
    CupomDescontoRepository cupomRepository;

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll().list();
    }

    @Override
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> findByClienteNome(String nome) {
        return pedidoRepository.findByClienteNome(nome);
    }

    @Override
    public List<Pedido> findByStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    @Override
    public List<Pedido> findByClienteId(Long idCliente) {
        return pedidoRepository.findByClienteId(idCliente);
    }

    @Override
    public List<Pedido> findByData(String data) {
        return pedidoRepository.findByData(data);
    }

    @Override
    @Transactional
    public PedidoResponseDTO create(PedidoRequestDTO dto) {

        Pedido pedido = PedidoMapper.toEntity(dto);

        // 🔹 Cliente
        Cliente cliente = clienteRepository.findById(dto.idCliente());
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }

        pedido.setCliente(cliente);
        pedido.setData(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        // 🔹 Itens
        for (int i = 0; i < pedido.getItens().size(); i++) {

            ItemPedido item = pedido.getItens().get(i);
            ItemPedidoRequestDTO itemDTO = dto.itens().get(i);

            Pamonha pamonha = pamonhaRepository.findById(itemDTO.idPamonha());

            if (pamonha == null) {
                throw new RuntimeException("Pamonha não encontrada");
            }

            item.setPamonha(pamonha);
            item.setPrecoUnitario(pamonha.getPreco());

            BigDecimal subtotal = pamonha.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            total = total.add(subtotal);
        }

        // 🔥 Cupom de desconto
        if (dto.codigoCupom() != null && !dto.codigoCupom().isBlank()) {

            CupomDesconto cupom = cupomRepository.findByCodigo(dto.codigoCupom());

            if (cupom == null) {
                throw new RuntimeException("Cupom não encontrado");
            }

            if (!cupom.getAtivo()) {
                throw new RuntimeException("Cupom inativo");
            }

            if (cupom.getDataValidade().isBefore(LocalDate.now())) {
                throw new RuntimeException("Cupom expirado");
            }

            pedido.setCupom(cupom);

            total = total.subtract(cupom.getValorDesconto());

            // evita total negativo
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                total = BigDecimal.ZERO;
            }
        }

        pedido.setTotal(total);

        pedidoRepository.persist(pedido);

        return PedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @Transactional
    public void update(Long id, PedidoRequestDTO dto) {

        Pedido pedido = findById(id);

        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }

        pedido.setStatus(StatusPedido.valueOf(dto.idStatusPedido()));

        pedidoRepository.persist(pedido);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        pedidoRepository.deleteById(id);
    }
}