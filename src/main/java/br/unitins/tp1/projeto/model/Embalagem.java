package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "embalagem")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Embalagem extends DefaultEntity {

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "custo")
    private BigDecimal custo;

    @Column(name = "peso_suportado")
    private double pesoSuportado;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public double getPesoSuportado() {
        return pesoSuportado;
    }

    public void setPesoSuportado(double pesoSuportado) {
        this.pesoSuportado = pesoSuportado;
    }
}
