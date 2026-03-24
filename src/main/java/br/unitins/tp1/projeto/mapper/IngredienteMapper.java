package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.dto.IngredienteResponseDTO;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.UnidadeMedida;

public class IngredienteMapper {

    public static Ingrediente toEnity(IngredienteRequestDTO dto) {
        
        if(dto == null) {
            return null;
        }

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome(dto.nome());
        ingrediente.setPrecoUnitario(dto.precoUnitario());
        ingrediente.setEstoque(dto.estoque());
        ingrediente.setUnidadeMedida(UnidadeMedida.valueOf(dto.idUnidadeMedida()));

        return ingrediente;
    }

    public static IngredienteResponseDTO toResponseDTO(Ingrediente ingrediente) {

        if(ingrediente == null) {
            return null;
        }

        return new IngredienteResponseDTO(
            ingrediente.getId(),
            ingrediente.getNome(),
            ingrediente.getPrecoUnitario(),
            ingrediente.getEstoque(),
            ingrediente.getUnidadeMedida()
        );
    }
}
