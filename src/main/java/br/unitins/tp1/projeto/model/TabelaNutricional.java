package br.unitins.tp1.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class TabelaNutricional {

    @Column(name = "valor_energetico")
    private double valorEnergetico;

    private double carboidratos;

    @Column(name = "acucares_totais")
    private double acucaresTotais;

    private double proteinas;

    @Column(name = "gorduras_total")
    private double gordurasTotal;

    @Column(name = "gorduras_saturadas")
    private double gordurasSaturadas;

    @Column(name = "gorduras_trans")
    private double gordurasTrans;

    @Column(name = "fibras_alimentares")
    private double fibrasAlimentares;

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
    public double getAcucaresTotais() {
        return acucaresTotais;
    }
    public void setAcucaresTotais(double acucaresTotais) {
        this.acucaresTotais = acucaresTotais;
    }
    public double getProteinas() {
        return proteinas;
    }
    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }
    public double getGordurasTotal() {
        return gordurasTotal;
    }
    public void setGordurasTotal(double gordurasTotal) {
        this.gordurasTotal = gordurasTotal;
    }
    public double getGordurasSaturadas() {
        return gordurasSaturadas;
    }
    public void setGordurasSaturadas(double gordurasSaturadas) {
        this.gordurasSaturadas = gordurasSaturadas;
    }
    public double getGordurasTrans() {
        return gordurasTrans;
    }
    public void setGordurasTrans(double gordurasTrans) {
        this.gordurasTrans = gordurasTrans;
    }
    public double getFibrasAlimentares() {
        return fibrasAlimentares;
    }
    public void setFibrasAlimentares(double fibrasAlimentares) {
        this.fibrasAlimentares = fibrasAlimentares;
    }
    public double getSodio() {
        return sodio;
    }
    public void setSodio(double sodio) {
        this.sodio = sodio;
    }

    
}
