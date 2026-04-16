package br.unitins.tp1.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "embalagem_plastica")
public class EmbalagemPlastica extends Embalagem {

    @Column(name = "tipo_plastico")
    private String tipoPlastico;

    @Column(name = "reciclavel")
    private boolean reciclavel;

    public String getTipoPlastico() {
        return tipoPlastico;
    }

    public void setTipoPlastico(String tipoPlastico) {
        this.tipoPlastico = tipoPlastico;
    }

    public boolean isReciclavel() {
        return reciclavel;
    }

    public void setReciclavel(boolean reciclavel) {
        this.reciclavel = reciclavel;
    }
}
