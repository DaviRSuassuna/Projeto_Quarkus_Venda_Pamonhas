package br.unitins.tp1.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario extends DefaultEntity {

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "senha")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private Perfil perfil;

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

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
