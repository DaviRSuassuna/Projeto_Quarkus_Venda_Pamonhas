package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;
import java.util.List;

public record PamonhaEcommerceResponseDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    List<CategoriaResponseDTO> categorias
) {}
