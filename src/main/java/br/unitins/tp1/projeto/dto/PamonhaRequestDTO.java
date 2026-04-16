package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.util.List;

public record PamonhaRequestDTO(
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    TabelaNutricionalRequestDTO tabelaNutricional,
    ModoPreparoRequestDTO modoPreparo,
    EmbalagemRequestDTO embalagem,
    List<CategoriaRequestDTO> categorias,
    List<ItemReceitaRequestDTO> itensReceita
) {}
