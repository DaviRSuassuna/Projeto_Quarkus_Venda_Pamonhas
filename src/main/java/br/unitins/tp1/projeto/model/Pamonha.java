package br.unitins.tp1.projeto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pamonha")
public class Pamonha extends DefaultEntity {

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "preco")
    private BigDecimal preco;

    @Column(name = "estoque")
    private Integer estoque;

    @Embedded
    private TabelaNutricional tabelaNutricional;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "modo_preparo_id")
    private ModoPreparo modoPreparo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "embalagem_id")
    private Embalagem embalagem;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "pamonha_categoria",
        joinColumns = @JoinColumn(name = "pamonha_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "pamonha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemReceita> itensReceita = new ArrayList<>();

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

    public TabelaNutricional getTabelaNutricional() {
        return tabelaNutricional;
    }

    public void setTabelaNutricional(TabelaNutricional tabelaNutricional) {
        this.tabelaNutricional = tabelaNutricional;
    }

    public ModoPreparo getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(ModoPreparo modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public Embalagem getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(Embalagem embalagem) {
        this.embalagem = embalagem;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<ItemReceita> getItensReceita() {
        return itensReceita;
    }

    public void setItensReceita(List<ItemReceita> itensReceita) {
        this.itensReceita = itensReceita;
    }
}
