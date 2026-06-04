package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.EnderecoRequestDTO;
import br.unitins.tp1.projeto.dto.EnderecoResponseDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Cidade;
import br.unitins.tp1.projeto.model.Endereco;
import br.unitins.tp1.projeto.model.PessoaFisica;
import br.unitins.tp1.projeto.repository.CidadeRepository;
import br.unitins.tp1.projeto.repository.EnderecoRepository;
import br.unitins.tp1.projeto.repository.PessoaFisicaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Inject
    CidadeRepository cidadeRepository;

    @Override
    public List<EnderecoResponseDTO> listarPorUsuario(String login) {
        return enderecoRepository.findByUsuario(login).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public EnderecoResponseDTO criar(String login, EnderecoRequestDTO dto) {
        PessoaFisica pf = pessoaFisicaRepository.findByUsuarioLogin(login);
        if (pf == null) throw new ValidationException("Usuário não possui perfil completo");
        Cidade cidade = cidadeRepository.findById(dto.cidadeId());
        if (cidade == null) throw new ValidationException("cidadeId", "Cidade não encontrada");
        Endereco endereco = new Endereco();
        endereco.setRua(dto.rua());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCep(dto.cep());
        endereco.setCidade(cidade);
        endereco.setPessoaFisica(pf);
        enderecoRepository.persist(endereco);
        return toResponseDTO(endereco);
    }

    @Override
    @Transactional
    public void deletar(String login, Long id) {
        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null) throw new jakarta.ws.rs.NotFoundException("Endereço não encontrado");
        if (!endereco.getPessoaFisica().getUsuario().getLogin().equals(login)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        enderecoRepository.delete(endereco);
    }

    private EnderecoResponseDTO toResponseDTO(Endereco e) {
        return new EnderecoResponseDTO(
            e.getId(),
            e.getRua(),
            e.getNumero(),
            e.getComplemento(),
            e.getBairro(),
            e.getCep(),
            e.getCidade() != null ? e.getCidade().getNome() : null,
            e.getCidade() != null && e.getCidade().getEstado() != null
                ? e.getCidade().getEstado().getNome() : null
        );
    }
}
