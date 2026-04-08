package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido extends DefaultEntity {

    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private BigDecimal total;

    // Cliente do pedido
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Endereço de entrega (snapshot)
    @ElementCollection
    private List<Endereco> enderecoEntrega = new ArrayList<>();

    // Itens do pedido
    @OneToMany(
        mappedBy = "pedido",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = jakarta.persistence.FetchType.EAGER
    )
    private List<ItemPedido> itens = new ArrayList<>();

    // =========================
    // GETTERS E SETTERS
    // =========================

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Endereco> getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(List<Endereco> enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}