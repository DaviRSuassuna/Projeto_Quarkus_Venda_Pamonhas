package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.SaborPamonha;

public class PamonhaMapper {

    public static Pamonha toEntity(PamonhaRequestDTO dto) {
        
        if (dto == null) {
            return null;
        }

        Pamonha pamonha = new Pamonha();
        pamonha.setIngredientePrincipal(dto.ingredientePrincipal());
        pamonha.setPreco(dto.preco());
        pamonha.setTemQueijo(dto.temQueijo());
        pamonha.setSaborPamonha(SaborPamonha.valueOf(dto.idSaborPamonha()));

        return pamonha;
    }

    public static PamonhaResponseDTO toResponseDTO(Pamonha pamonha) {

        if (pamonha == null) {
            return null;
        }

        return new PamonhaResponseDTO(pamonha.getId(), pamonha.getIngredientePrincipal(), pamonha.getPreco(), pamonha.getTemQueijo(), pamonha.getSaborPamonha());
    }
}
