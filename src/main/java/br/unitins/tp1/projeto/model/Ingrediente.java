package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingrediente")
public class Ingrediente extends DefaultEntity{

    private String nome;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario; //Documentação, valor por kg
    private double estoque;
    
    @Column(name = "unidade_medida")
    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

    @Embedded
    private TabelaNutricional tabelaNutricional;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getEstoque() {
        return estoque;
    }

    public void setEstoque(double estoque) {
        this.estoque = estoque;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public TabelaNutricional getTabelaNutricional() {
        return tabelaNutricional;
    }

    public void setTabelaNutricional(TabelaNutricional tabelaNutricional) {
        this.tabelaNutricional = tabelaNutricional;
    }

    
}
