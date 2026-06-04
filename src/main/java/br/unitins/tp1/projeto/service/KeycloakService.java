package br.unitins.tp1.projeto.service;

public interface KeycloakService {

    String registrarUsuario(String login, String senha, String role);

    void atualizarSenha(String keycloakId, String novaSenha);

    void deletarUsuario(String keycloakId);
}
