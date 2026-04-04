package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

public record PamonhaRequestDTO(
    String nome, 
    String descricao, 
    BigDecimal preco, 
    Integer estoque, 
    Long idSaborPamonha, 
    Long idTipoPamonha,
    ReceitaRequestDTO receita
) {}