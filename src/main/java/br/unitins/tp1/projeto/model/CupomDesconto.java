package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cupom_desconto")
public class CupomDesconto extends DefaultEntity {

    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @ManyToMany
    @JoinTable(
        name = "cupom_pamonha",
        joinColumns = @JoinColumn(name = "cupom_id"),
        inverseJoinColumns = @JoinColumn(name = "pamonha_id")
    )
    private List<Pamonha> pamonhas = new ArrayList<>();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public List<Pamonha> getPamonhas() {
        return pamonhas;
    }

    public void setPamonhas(List<Pamonha> pamonhas) {
        this.pamonhas = pamonhas;
    }
}
