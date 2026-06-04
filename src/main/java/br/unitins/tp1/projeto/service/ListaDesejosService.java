package br.unitins.tp1.projeto.service;

import br.unitins.tp1.projeto.dto.ListaDesejosResponseDTO;

public interface ListaDesejosService {

    ListaDesejosResponseDTO buscarPorUsuario(String login);

    void adicionarPamonha(String login, Long pamonhaId);

    void removerPamonha(String login, Long pamonhaId);
}
