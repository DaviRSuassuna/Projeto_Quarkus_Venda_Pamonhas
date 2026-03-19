package br.unitins.tp1.projeto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Pamonha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ingredientePrincipal;
    private Double preco;
    private Boolean temQueijo;
    private LocalDateTime dataCadastro;

    @Column(name = "sabor_pamonha")
    @Enumerated(EnumType.STRING)
    private SaborPamonha saborPamonha;

    @PrePersist
    private void preencherDataCadastro() {
        setDataCadastro(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredientePrincipal() {
        return ingredientePrincipal;
    }

    public void setIngredientePrincipal(String ingredientePrincipal) {
        this.ingredientePrincipal = ingredientePrincipal;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Boolean getTemQueijo() {
        return temQueijo;
    }

    public void setTemQueijo(Boolean temQueijo) {
        this.temQueijo = temQueijo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public SaborPamonha getSaborPamonha() {
        return saborPamonha;
    }

    public void setSaborPamonha(SaborPamonha saborPamonha) {
        this.saborPamonha = saborPamonha;
    }  
}