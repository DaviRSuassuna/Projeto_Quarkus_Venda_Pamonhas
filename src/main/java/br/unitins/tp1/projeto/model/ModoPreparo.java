package br.unitins.tp1.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "modo_preparo")
public class ModoPreparo extends DefaultEntity {

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tempo_preparo_minutos")
    private int tempoPreparoMinutos;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTempoPreparoMinutos() {
        return tempoPreparoMinutos;
    }

    public void setTempoPreparoMinutos(int tempoPreparoMinutos) {
        this.tempoPreparoMinutos = tempoPreparoMinutos;
    }
}
