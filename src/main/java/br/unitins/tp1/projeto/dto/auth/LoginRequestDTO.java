package br.unitins.tp1.projeto.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String senha;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
