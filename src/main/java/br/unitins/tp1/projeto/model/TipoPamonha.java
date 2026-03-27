package br.unitins.tp1.projeto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum TipoPamonha {
    TRADICIONAL(1L, "Tradicional"),
    COM_QUEIJO(2L, "Com_queijo"),
    GOURMET(3L, "Gourmet");

    private final Long ID;
    private final String NOME;

    TipoPamonha(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static TipoPamonha valueOf(Long id) {
        if(id == null) {
            return null;
        }
        for(TipoPamonha tipo : TipoPamonha.values()) {
            if(tipo.getID().equals(id)) {
                return tipo;
            }
        }
        return null;
    }
}
