package br.unitins.tp1.projeto.dto;

import java.math.BigDecimal;

import br.unitins.tp1.projeto.model.SaborPamonha;
import br.unitins.tp1.projeto.model.TipoPamonha;

public record PamonhaResponseDTO(
    Long id, 
    String nome, 
    String descricao, 
    BigDecimal preco, 
    Integer estoque, 
    SaborPamonha saborPamonha, 
    TipoPamonha tipoPamonha,
    ReceitaResponseDTO receita
) {}