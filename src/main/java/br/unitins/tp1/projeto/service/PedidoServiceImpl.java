package br.unitins.tp1.projeto.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.unitins.tp1.projeto.dto.ItemRequestDTO;
import br.unitins.tp1.projeto.dto.ItemResponseDTO;
import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.CupomDesconto;
import br.unitins.tp1.projeto.model.Endereco;
import br.unitins.tp1.projeto.model.EnderecoEntrega;
import br.unitins.tp1.projeto.model.Item;
import br.unitins.tp1.projeto.model.Pagamento;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.Pedido;
import br.unitins.tp1.projeto.model.PessoaFisica;
import br.unitins.tp1.projeto.model.StatusPagamento;
import br.unitins.tp1.projeto.model.StatusPedido;
import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.CupomDescontoRepository;
import br.unitins.tp1.projeto.repository.EnderecoRepository;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import br.unitins.tp1.projeto.repository.PedidoRepository;
import br.unitins.tp1.projeto.repository.PessoaFisicaRepository;
import br.unitins.tp1.projeto.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    PamonhaRepository pamonhaRepository;

    @Inject
    CupomDescontoRepository cupomDescontoRepository;

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Override
    @Transactional
    public PedidoResponseDTO criar(String login, PedidoRequestDTO dto) {
        Usuario usuario = usuarioRepository.find("email", login).firstResult();
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");

        PessoaFisica pessoaFisica = pessoaFisicaRepository.findByUsuarioEmail(login);
        if (pessoaFisica == null) {
            throw new ValidationException("cadastro", "É necessário ter cadastro completo (dados pessoais) para realizar pedidos. Complete seu perfil antes de comprar.");
        }

        Endereco endereco = enderecoRepository.findById(dto.enderecoId());
        if (endereco == null) throw new ValidationException("enderecoId", "Endereço não encontrado");
        if (!endereco.getPessoaFisica().getUsuario().getEmail().equals(login)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        List<Item> itens = new ArrayList<>();
        for (ItemRequestDTO itemDto : dto.itens()) {
            Pamonha pamonha = pamonhaRepository.findById(itemDto.pamonhaId());
            if (pamonha == null) throw new ValidationException("pamonhaId", "Pamonha não encontrada");
            if (pamonha.getEstoque() < itemDto.quantidade()) {
                throw new ValidationException("quantidade", "Estoque insuficiente para: " + pamonha.getNome());
            }
            Item item = new Item();
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(pamonha.getPreco());
            item.setDescontoUnitario(BigDecimal.ZERO);
            item.setPamonha(pamonha);
            itens.add(item);
        }

        BigDecimal totalBruto = itens.stream()
                .map(i -> i.getValorUnitario().multiply(new BigDecimal(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CupomDesconto cupom = null;
        BigDecimal desconto = BigDecimal.ZERO;
        if (dto.cupomId() != null) {
            cupom = cupomDescontoRepository.findById(dto.cupomId());
            if (cupom == null) throw new ValidationException("cupomId", "Cupom não encontrado");
            if (cupom.getDataValidade().isBefore(LocalDate.now())) {
                throw new ValidationException("cupomId", "Cupom expirado");
            }
            desconto = cupom.getValorDesconto();
        }

        BigDecimal total = totalBruto.subtract(desconto);
        if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;

        EnderecoEntrega enderecoEntrega = new EnderecoEntrega();
        enderecoEntrega.setRua(endereco.getRua());
        enderecoEntrega.setNumero(endereco.getNumero());
        enderecoEntrega.setComplemento(endereco.getComplemento());
        enderecoEntrega.setBairro(endereco.getBairro());
        enderecoEntrega.setCep(endereco.getCep());
        enderecoEntrega.setCidade(endereco.getCidade());

        Pagamento pagamento = new Pagamento();
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setValor(total);
        pagamento.setFormaPagamento(dto.formaPagamento());
        pagamento.setDataPagamento(null);

        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setTotal(total);
        pedido.setDesconto(desconto);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setUsuario(usuario);
        pedido.setCupom(cupom);
        pedido.setEnderecoEntrega(enderecoEntrega);
        pedido.setPagamento(pagamento);

        for (Item item : itens) {
            item.setPedido(pedido);
            pedido.getItens().add(item);
            Pamonha pamonha = item.getPamonha();
            pamonha.setEstoque(pamonha.getEstoque() - item.getQuantidade());
        }

        pedidoRepository.persist(pedido);
        return toPedidoResponseDTO(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listarPorUsuario(String login, int page, int size) {
        return pedidoRepository.find("usuario.email", login)
                .page(page, size).list().stream()
                .map(this::toPedidoResponseDTO)
                .toList();
    }

    @Override
    public PedidoResponseDTO buscarPorId(String login, Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) throw new NotFoundException("Pedido não encontrado");
        if (!pedido.getUsuario().getEmail().equals(login)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        return toPedidoResponseDTO(pedido);
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) throw new NotFoundException("Pedido não encontrado");
        pedido.setStatus(novoStatus);
        pedidoRepository.persist(pedido);
    }

    private PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        List<ItemResponseDTO> itensDto = pedido.getItens().stream()
                .map(item -> new ItemResponseDTO(
                    item.getId(),
                    item.getPamonha().getNome(),
                    item.getQuantidade(),
                    item.getValorUnitario(),
                    item.getDescontoUnitario()
                ))
                .toList();
        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getDataCriacao(),
            pedido.getTotal(),
            pedido.getDesconto(),
            pedido.getStatus().name(),
            itensDto,
            pedido.getPagamento() != null ? pedido.getPagamento().getFormaPagamento().name() : null,
            pedido.getPagamento() != null ? pedido.getPagamento().getStatus().name() : null
        );
    }
}
