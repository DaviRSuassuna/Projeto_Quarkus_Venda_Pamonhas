package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.model.Aluno;

public interface AlunoService {
    List<Aluno> findAll();
    Aluno findById(Long id);
    List<Aluno> findByNome(String nome);
    Aluno create(Aluno aluno);
    void update(Long id, Aluno aluno);
    void delete(Long id);
}
