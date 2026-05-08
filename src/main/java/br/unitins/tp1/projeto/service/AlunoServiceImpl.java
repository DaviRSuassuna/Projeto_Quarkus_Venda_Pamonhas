package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.model.Aluno;
import br.unitins.tp1.projeto.model.PessoaFisica;
import br.unitins.tp1.projeto.repository.AlunoRepository;
import br.unitins.tp1.projeto.repository.PessoaFisicaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AlunoServiceImpl implements AlunoService {

    @Inject
    AlunoRepository repository;

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Override
    public List<Aluno> findAll() {
        return repository.findAll().list();
    }

    @Override
    public Aluno findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Aluno> findByNome(String nome) {
        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public Aluno create(Aluno aluno) {
        PessoaFisica pessoaFisica = aluno.getPessoaFisica();
        if (pessoaFisica != null && pessoaFisica.getId() == null) {
            pessoaFisicaRepository.persist(pessoaFisica);
        }

        repository.persist(aluno);
        return aluno;
    }

    @Override
    @Transactional
    public void update(Long id, Aluno aluno) {
        Aluno a = findById(id);
        a.setMatricula(aluno.getMatricula());

        PessoaFisica pessoaFisica = a.getPessoaFisica();
        if (pessoaFisica == null) {
            pessoaFisica = new PessoaFisica();
            a.setPessoaFisica(pessoaFisica);
        }

        if (aluno.getPessoaFisica() != null) {
            pessoaFisica.setNome(aluno.getPessoaFisica().getNome());
            pessoaFisica.setEmail(aluno.getPessoaFisica().getEmail());
            pessoaFisica.setDataNascimento(aluno.getPessoaFisica().getDataNascimento());
            pessoaFisica.setCpf(aluno.getPessoaFisica().getCpf());

            if (pessoaFisica.getId() == null) {
                pessoaFisicaRepository.persist(pessoaFisica);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
