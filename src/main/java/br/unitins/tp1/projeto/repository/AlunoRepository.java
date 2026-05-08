package br.unitins.tp1.projeto.repository;

import br.unitins.tp1.projeto.model.Aluno;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlunoRepository implements PanacheRepository<Aluno> {

    public PanacheQuery<Aluno> findByNome(String nome) {
        return find("SELECT a FROM Aluno a WHERE UPPER(a.pessoaFisica.nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public Aluno findByMatricula(String matricula) {
        return find("matricula", matricula).firstResult();
    }

    public Aluno findByCpf(String cpf) {
        return find("pessoaFisica.cpf", cpf).firstResult();
    }

}
