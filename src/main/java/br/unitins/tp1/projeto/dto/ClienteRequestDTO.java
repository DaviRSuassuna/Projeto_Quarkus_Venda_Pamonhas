package br.unitins.tp1.projeto.dto;

import java.time.LocalDate;
import java.util.List;

public record ClienteRequestDTO(
    String nome,
    String email,
    String telefone,
    String cpf,
    LocalDate dataNascimento,
    List<EnderecoDTO> enderecos
) {}
