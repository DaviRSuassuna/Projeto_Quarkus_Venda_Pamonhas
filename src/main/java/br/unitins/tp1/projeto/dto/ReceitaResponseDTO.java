package br.unitins.tp1.projeto.dto;

import java.util.List;

public record ReceitaResponseDTO(
    String descricao,
    List<ItemReceitaResponseDTO> itens
) {}
