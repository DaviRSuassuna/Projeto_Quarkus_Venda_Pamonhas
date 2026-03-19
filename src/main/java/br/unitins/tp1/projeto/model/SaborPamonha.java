package br.unitins.tp1.projeto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum SaborPamonha {
    DOCE(1L, "Doce"),
    SALGADA(2L, "Salgada");

    private final Long ID;
    private final String NOME;

    SaborPamonha(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static SaborPamonha valueOf(Long id) {
        if(id == null) {
            return null;
        }
        for(SaborPamonha sabor : SaborPamonha.values()) {
            if(sabor.getID().equals(id)) {
                return sabor;
            }
        }
        return null;
    }
}
