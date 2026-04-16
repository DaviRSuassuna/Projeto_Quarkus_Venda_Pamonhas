package br.unitins.tp1.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "embalagem_biodegradavel")
public class EmbalagemBiodegradavel extends Embalagem {

    @Column(name = "material")
    private String material;

    @Column(name = "tempo_decomposicao_dias")
    private int tempoDecomposicaoDias;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getTempoDecomposicaoDias() {
        return tempoDecomposicaoDias;
    }

    public void setTempoDecomposicaoDias(int tempoDecomposicaoDias) {
        this.tempoDecomposicaoDias = tempoDecomposicaoDias;
    }
}
