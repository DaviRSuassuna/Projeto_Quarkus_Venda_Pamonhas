package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.util.List;

public record PamonhaResponseDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    TabelaNutricionalResponseDTO tabelaNutricional,
    ModoPreparoResponseDTO modoPreparo,
    EmbalagemResponseDTO embalagem,
    List<CategoriaResponseDTO> categorias,
    List<ItemReceitaResponseDTO> itensReceita
) {}
