package br.unitins.tp1.projeto.dto;

import java.util.List;

public record AtualizarUsuarioAdminRequestDTO(
    List<String> perfis,
    Boolean ativo
) {}
