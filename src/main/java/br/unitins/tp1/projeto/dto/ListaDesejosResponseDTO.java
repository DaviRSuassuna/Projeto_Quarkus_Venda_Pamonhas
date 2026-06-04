package br.unitins.tp1.projeto.dto;

import java.util.List;

public record ListaDesejosResponseDTO(
    Long id,
    List<PamonhaEcommerceResponseDTO> pamonhas
) {}
