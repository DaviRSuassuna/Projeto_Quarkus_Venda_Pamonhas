package br.unitins.tp1.projeto.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AlunoRequestDTO(
    @NotBlank(message = "O nome do aluno é obrigatório")
    @Size(min = 3, max = 100, message = "O nome do aluno deve conter entre 3 e 100 caracteres")
    String nome,

    @NotBlank(message = "O email do aluno é obrigatório")
    @Email(message = "O email do aluno deve ser válido")
    String email,

    @NotBlank(message = "A matrícula do aluno é obrigatória")
    @Size(max = 30, message = "A matrícula do aluno deve conter no máximo 30 caracteres")
    String matricula,

    @NotNull(message = "A data de nascimento do aluno é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    LocalDate dataNascimento,

    @NotBlank(message = "O CPF do aluno é obrigatório")
    @Pattern(
        regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
        message = "O CPF deve estar no formato 000.000.000-00"
    )
    String cpf) {
}
