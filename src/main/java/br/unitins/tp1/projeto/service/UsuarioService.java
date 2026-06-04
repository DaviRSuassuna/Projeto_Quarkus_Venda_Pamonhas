package br.unitins.tp1.projeto.service;

import br.unitins.tp1.projeto.dto.AlterarSenhaRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroCompletoRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroSimplesRequestDTO;
import br.unitins.tp1.projeto.dto.EditarUsuarioRequestDTO;
import br.unitins.tp1.projeto.dto.UsuarioResponseDTO;

public interface UsuarioService {

    void cadastrarSimples(CadastroSimplesRequestDTO dto);

    void cadastrarCompleto(CadastroCompletoRequestDTO dto);

    UsuarioResponseDTO buscarDadosLogado(String login);

    void editarDados(String login, EditarUsuarioRequestDTO dto);

    void alterarSenha(String login, AlterarSenhaRequestDTO dto);
}
