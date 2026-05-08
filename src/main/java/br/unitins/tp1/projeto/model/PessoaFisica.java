package br.unitins.tp1.projeto.model;

import jakarta.persistence.Entity;

@Entity
public class PessoaFisica extends Pessoa {

    private String sobrenome;
    private String cpf;

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
