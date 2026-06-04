package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotNull;

public record ItemRequestDTO(
    @NotNull Long pamonhaId,
    @NotNull Integer quantidade
) {}
