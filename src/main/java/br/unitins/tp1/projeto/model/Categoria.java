package br.unitins.tp1.projeto.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria extends DefaultEntity {

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToMany(mappedBy = "categorias")
    private List<Pamonha> pamonhas = new ArrayList<>();

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

    public List<Pamonha> getPamonhas() {
        return pamonhas;
    }

    public void setPamonhas(List<Pamonha> pamonhas) {
        this.pamonhas = pamonhas;
    }
}
