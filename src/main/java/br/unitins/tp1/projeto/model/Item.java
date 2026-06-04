package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_pedido")
public class Item extends DefaultEntity {

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;

    @Column(name = "desconto_unitario")
    private BigDecimal descontoUnitario;

    @ManyToOne
    @JoinColumn(name = "pamonha_id")
    private Pamonha pamonha;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getDescontoUnitario() {
        return descontoUnitario;
    }

    public void setDescontoUnitario(BigDecimal descontoUnitario) {
        this.descontoUnitario = descontoUnitario;
    }

    public Pamonha getPamonha() {
        return pamonha;
    }

    public void setPamonha(Pamonha pamonha) {
        this.pamonha = pamonha;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
