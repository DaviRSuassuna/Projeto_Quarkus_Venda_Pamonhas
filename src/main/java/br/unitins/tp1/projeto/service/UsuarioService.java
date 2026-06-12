package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.AtualizarUsuarioAdminRequestDTO;
import br.unitins.tp1.projeto.dto.EditarUsuarioRequestDTO;
import br.unitins.tp1.projeto.dto.UsuarioResponseDTO;

public interface UsuarioService {

    UsuarioResponseDTO buscarDadosLogado(String login);

    void editarDados(String login, EditarUsuarioRequestDTO dto);

    List<UsuarioResponseDTO> listarTodos(int page, int size);

    void atualizarUsuarioAdmin(Long id, AtualizarUsuarioAdminRequestDTO dto);
}
