package br.unitins.tp1.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class TabelaNutricional {

    @Column(name = "valor_energetico")
    private double valorEnergetico;

    @Column(name = "carboidratos")
    private double carboidratos;

    @Column(name = "proteinas")
    private double proteinas;

    @Column(name = "gorduras_totais")
    private double gordurasTotais;

    @Column(name = "fibras")
    private double fibras;

    @Column(name = "sodio")
    private double sodio;

    public double getValorEnergetico() {
        return valorEnergetico;
    }

    public void setValorEnergetico(double valorEnergetico) {
        this.valorEnergetico = valorEnergetico;
    }

    public double getCarboidratos() {
        return carboidratos;
    }

    public void setCarboidratos(double carboidratos) {
        this.carboidratos = carboidratos;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGordurasTotais() {
        return gordurasTotais;
    }

    public void setGordurasTotais(double gordurasTotais) {
        this.gordurasTotais = gordurasTotais;
    }

    public double getFibras() {
        return fibras;
    }

    public void setFibras(double fibras) {
        this.fibras = fibras;
    }

    public double getSodio() {
        return sodio;
    }

    public void setSodio(double sodio) {
        this.sodio = sodio;
    }
}

