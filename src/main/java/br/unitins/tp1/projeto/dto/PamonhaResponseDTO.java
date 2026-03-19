package br.unitins.tp1.projeto.dto;

import br.unitins.tp1.projeto.model.SaborPamonha;

public record PamonhaResponseDTO(Long id, String ingredientePrincipal, Double preco, Boolean temQueijo, SaborPamonha saborPamonha) {

}
