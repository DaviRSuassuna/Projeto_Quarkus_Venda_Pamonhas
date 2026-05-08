package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.AlunoRequestDTO;
import br.unitins.tp1.projeto.dto.AlunoResponseDTO;
import br.unitins.tp1.projeto.model.Aluno;
import br.unitins.tp1.projeto.model.PessoaFisica;

public class AlunoMapper {

    public static Aluno toEntity(AlunoRequestDTO dto) {
        if (dto == null)
            return null;

        Aluno aluno = new Aluno();
        aluno.setMatricula(dto.matricula());

        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setNome(dto.nome());
        pessoaFisica.setEmail(dto.email());
        pessoaFisica.setDataNascimento(dto.dataNascimento());
        pessoaFisica.setCpf(dto.cpf());

        aluno.setPessoaFisica(pessoaFisica);

        return aluno;
    }

    public static AlunoResponseDTO toResponseDTO(Aluno aluno) {
        if (aluno == null)
            return null;

        PessoaFisica pessoaFisica = aluno.getPessoaFisica();

        return new AlunoResponseDTO(
            aluno.getId(),
            pessoaFisica != null ? pessoaFisica.getNome() : null,
            pessoaFisica != null ? pessoaFisica.getEmail() : null,
            aluno.getMatricula(),
            pessoaFisica != null ? pessoaFisica.getDataNascimento() : null,
            pessoaFisica != null ? pessoaFisica.getCpf() : null
        );
    }

}
