package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PamonhaRequestDTO(
    @NotBlank String nome,
    @NotBlank String descricao,
    @NotNull BigDecimal preco,
    @NotNull Integer estoque,
    @Valid TabelaNutricionalRequestDTO tabelaNutricional,
    @Valid ModoPreparoRequestDTO modoPreparo,
    @Valid EmbalagemRequestDTO embalagem,
    @Valid List<CategoriaRequestDTO> categorias,
    @Valid List<ItemReceitaRequestDTO> itensReceita
) {}
