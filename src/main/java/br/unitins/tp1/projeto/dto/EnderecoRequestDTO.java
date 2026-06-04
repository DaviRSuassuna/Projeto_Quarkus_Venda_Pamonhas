package br.unitins.tp1.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequestDTO(
    @NotBlank String rua,
    @NotBlank String numero,
    String complemento,
    @NotBlank String bairro,
    @NotBlank String cep,
    @NotNull Long cidadeId
) {}
