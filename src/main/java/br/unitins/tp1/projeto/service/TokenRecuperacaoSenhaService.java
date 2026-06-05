package br.unitins.tp1.projeto.service;

import br.unitins.tp1.projeto.dto.RedefinirSenhaRequestDTO;

public interface TokenRecuperacaoSenhaService {

    String solicitarRecuperacao(String email);

    void redefinirSenha(RedefinirSenhaRequestDTO dto);
}
