package br.unitins.tp1.projeto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum UnidadeMedida {
    KG(1L, "Quilograma"),
    GRAMA(2L, "Grama"),
    LITRO(3L, "Litro"),
    ML(4L, "Mililitro"),
    UNIDADE(5L, "Unidade sem valor");

    private final Long ID;
    private final String NOME;

    UnidadeMedida(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static UnidadeMedida valueOf(Long id) {
        if(id == null) {
            return null;
        }
        for(UnidadeMedida unidade : UnidadeMedida.values()) {
            if(unidade.getID().equals(id)) {
                return unidade;
            }
        }
        return null;
    }
}
