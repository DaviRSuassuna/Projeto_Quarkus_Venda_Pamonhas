package br.unitins.tp1.projeto.dto;

import java.util.List;

public record ReceitaRequestDTO(
    String descricao,
    List<ItemReceitaRequestDTO> itens
) {}
