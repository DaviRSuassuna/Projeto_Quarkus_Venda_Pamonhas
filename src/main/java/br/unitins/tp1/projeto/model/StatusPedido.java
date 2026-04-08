package br.unitins.tp1.projeto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum StatusPedido {
    
    ABERTO(1L, "Aberto"),
    PAGO(2L, "Pago"),
    CANCELADO(3L, "Cancelado");

    private final Long ID;
    private final String NOME;

    StatusPedido(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static StatusPedido valueOf(Long id) {
        if (id == null) {
            return null;
        }

        for (StatusPedido status : StatusPedido.values()) {
            if (status.getID().equals(id)) {
                return status;
            }
        }

        return null;
    }
}