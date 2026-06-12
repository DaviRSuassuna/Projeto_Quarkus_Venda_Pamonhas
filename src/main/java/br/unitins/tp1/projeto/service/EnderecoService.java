package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.EnderecoRequestDTO;
import br.unitins.tp1.projeto.dto.EnderecoResponseDTO;

public interface EnderecoService {

    List<EnderecoResponseDTO> listarPorUsuario(String login, int page, int size);

    EnderecoResponseDTO criar(String login, EnderecoRequestDTO dto);

    EnderecoResponseDTO atualizar(String login, Long id, EnderecoRequestDTO dto);

    void deletar(String login, Long id);
}
