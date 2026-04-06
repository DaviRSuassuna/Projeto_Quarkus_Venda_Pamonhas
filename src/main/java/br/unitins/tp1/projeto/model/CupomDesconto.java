package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cupom_desconto")
public class CupomDesconto extends DefaultEntity{

    private String codigo;

    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @Column(name = "data_validade")
    private LocalDate dataValidade;
    private Boolean ativo;
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    
}
