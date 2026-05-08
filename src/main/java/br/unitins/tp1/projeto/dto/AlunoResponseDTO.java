package br.unitins.tp1.projeto.dto;

import java.time.LocalDate;

public record AlunoResponseDTO(
    Long id,
    String nome,
    String email,
    String matricula,
    LocalDate dataNascimento,
    String cpf) {
}
