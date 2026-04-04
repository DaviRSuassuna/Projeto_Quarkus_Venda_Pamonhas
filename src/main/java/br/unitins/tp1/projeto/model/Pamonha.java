package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pamonha")
public class Pamonha extends DefaultEntity {

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;

    @Column(name = "sabor_pamonha")
    @Enumerated(EnumType.STRING)
    private SaborPamonha saborPamonha;

    @Column(name = "tipo_pamonha")
    @Enumerated(EnumType.STRING)
    private TipoPamonha tipoPamonha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receita_id")
    private Receita receita;

    @Embedded
    private TabelaNutricional tabelaNutricional;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public SaborPamonha getSaborPamonha() {
        return saborPamonha;
    }

    public void setSaborPamonha(SaborPamonha saborPamonha) {
        this.saborPamonha = saborPamonha;
    }

    public TipoPamonha getTipoPamonha() {
        return tipoPamonha;
    }

    public void setTipoPamonha(TipoPamonha tipoPamonha) {
        this.tipoPamonha = tipoPamonha;
    }

    public TabelaNutricional getTabelaNutricional() {
        return tabelaNutricional;
    }

    public void setTabelaNutricional(TabelaNutricional tabelaNutricional) {
        this.tabelaNutricional = tabelaNutricional;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    

}