package br.unitins.tp1.projeto.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "receita")
public class Receita extends DefaultEntity {

    private String descricao;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemReceita> itens = new ArrayList<>();

    public TabelaNutricional calcularTabelaNutricional() {
        
        TabelaNutricional total = new TabelaNutricional();

        for (ItemReceita itemReceita : itens) {
            TabelaNutricional t = itemReceita.getIngrediente().getTabelaNutricional();

            total.setValorEnergetico(
                total.getValorEnergetico() + t.getValorEnergetico() * itemReceita.getQuantidade()
            );

            total.setCarboidratos(
                total.getCarboidratos() + t.getCarboidratos() * itemReceita.getQuantidade()
            );

            total.setAcucaresTotais(
                total.getAcucaresTotais() + t.getAcucaresTotais() * itemReceita.getQuantidade()
            );

            total.setProteinas(
                total.getProteinas() + t.getProteinas() * itemReceita.getQuantidade()
            );

            total.setGordurasTotal(
                total.getGordurasTotal() + t.getGordurasTotal() * itemReceita.getQuantidade()
            );

            total.setGordurasSaturadas(
                total.getGordurasSaturadas() + t.getGordurasSaturadas() * itemReceita.getQuantidade()
            );

            total.setGordurasTrans(
                total.getGordurasTrans() + t.getGordurasTrans() * itemReceita.getQuantidade()
            );

            total.setFibrasAlimentares(
                total.getFibrasAlimentares() + t.getFibrasAlimentares() * itemReceita.getQuantidade()
            );

            total.setSodio(
                total.getSodio() + t.getSodio() * itemReceita.getQuantidade()
            );
        }

        return total;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ItemReceita> getItens() {
        return itens;
    }

    public void setItens(List<ItemReceita> itens) {
        this.itens = itens;
    }
}
