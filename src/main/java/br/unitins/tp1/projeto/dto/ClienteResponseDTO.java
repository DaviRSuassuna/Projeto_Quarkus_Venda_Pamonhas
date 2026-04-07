package br.unitins.tp1.projeto.dto;

import java.time.LocalDate;
import java.util.List;

public record ClienteResponseDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    String cpf,
    LocalDate dataNascimento,
    List<EnderecoDTO> enderecos
) {}
