package br.unitins.tp1.projeto.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lista_desejos")
public class ListaDesejos extends DefaultEntity {

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "lista_desejos_pamonha",
        joinColumns = @JoinColumn(name = "lista_desejos_id"),
        inverseJoinColumns = @JoinColumn(name = "pamonha_id")
    )
    private List<Pamonha> pamonhas = new ArrayList<>();

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Pamonha> getPamonhas() {
        return pamonhas;
    }

    public void setPamonhas(List<Pamonha> pamonhas) {
        this.pamonhas = pamonhas;
    }
}
