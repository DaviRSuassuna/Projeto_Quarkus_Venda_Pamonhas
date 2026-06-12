package br.unitins.tp1.projeto.service;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.unitins.tp1.projeto.dto.AtualizarUsuarioAdminRequestDTO;
import br.unitins.tp1.projeto.dto.EditarUsuarioRequestDTO;
import br.unitins.tp1.projeto.dto.UsuarioResponseDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.ListaDesejos;
import br.unitins.tp1.projeto.model.Perfil;
import br.unitins.tp1.projeto.model.PessoaFisica;
import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.ListaDesejosRepository;
import br.unitins.tp1.projeto.repository.PessoaFisicaRepository;
import br.unitins.tp1.projeto.repository.UsuarioRepository;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger LOG = Logger.getLogger(UsuarioServiceImpl.class);

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Inject
    ListaDesejosRepository listaDesejosRepository;

    @Inject
    KeycloakService keycloakService;

    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityIdentity identity;

    private Usuario garantirUsuarioLocal(String login) {
        Usuario usuario = usuarioRepository.find("email", login).firstResult();
        if (usuario != null) return usuario;

        usuario = new Usuario();
        usuario.setEmail(login);
        usuario.setKeycloakId(jwt.getSubject());

        List<Perfil> perfis = identity.getRoles().stream()
                .map(role -> {
                    try { return Perfil.valueOf(role); }
                    catch (IllegalArgumentException e) { return null; }
                })
                .filter(java.util.Objects::nonNull)
                .toList();
        usuario.setPerfis(perfis.isEmpty() ? List.of(Perfil.ROLE_USER) : perfis);

        usuarioRepository.persist(usuario);

        ListaDesejos lista = new ListaDesejos();
        lista.setUsuario(usuario);
        listaDesejosRepository.persist(lista);

        return usuario;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO buscarDadosLogado(String login) {
        Usuario usuario = garantirUsuarioLocal(login);
        PessoaFisica pf = pessoaFisicaRepository.findByUsuarioEmail(login);
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getEmail(),
            pf != null ? pf.getNome() : null,
            pf != null ? pf.getEmail() : null
        );
    }

    @Override
    @Transactional
    public void editarDados(String login, EditarUsuarioRequestDTO dto) {
        Usuario usuario = garantirUsuarioLocal(login);

        PessoaFisica pf = pessoaFisicaRepository.findByUsuarioEmail(login);
        boolean novo = false;
        if (pf == null) {
            pf = new PessoaFisica();
            pf.setUsuario(usuario);
            novo = true;
        }

        if (dto.email() != null && !dto.email().equals(usuario.getEmail())) {
            PessoaFisica existente = pessoaFisicaRepository.findByEmail(dto.email());
            if (existente != null && !existente.getUsuario().getId().equals(usuario.getId())) {
                throw new ValidationException("email", "Email já cadastrado");
            }
            usuario.setEmail(dto.email());
            pf.setEmail(dto.email());
            usuarioRepository.persist(usuario);
            if (usuario.getKeycloakId() != null) {
                try {
                    keycloakService.atualizarEmail(usuario.getKeycloakId(), dto.email());
                } catch (Exception e) {
                    LOG.warnf("Não foi possível atualizar e-mail no Keycloak para '%s': %s", login, e.getMessage());
                }
            }
        } else if (dto.email() != null) {
            pf.setEmail(dto.email());
        }

        if (dto.nome() != null) pf.setNome(dto.nome());
        if (dto.sobrenome() != null) pf.setSobrenome(dto.sobrenome());
        if (dto.telefone() != null) pf.setTelefone(dto.telefone());
        if (dto.cpf() != null) pf.setCpf(dto.cpf());
        if (dto.dataNascimento() != null) pf.setDataNascimento(dto.dataNascimento());

        pessoaFisicaRepository.persist(pf);
    }

    @Override
    public List<UsuarioResponseDTO> listarTodos(int page, int size) {
        return usuarioRepository.findAll().page(page, size).list().stream()
                .map(u -> {
                    PessoaFisica pf = pessoaFisicaRepository.findByUsuarioEmail(u.getEmail());
                    return new UsuarioResponseDTO(
                        u.getId(),
                        u.getEmail(),
                        pf != null ? pf.getNome() : null,
                        pf != null ? pf.getEmail() : null
                    );
                })
                .toList();
    }

    @Override
    @Transactional
    public void atualizarUsuarioAdmin(Long id, AtualizarUsuarioAdminRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");

        if (dto.perfis() != null) {
            List<Perfil> novosPerfis = dto.perfis().stream().map(p -> {
                try {
                    return Perfil.valueOf(p);
                } catch (IllegalArgumentException e) {
                    throw new ValidationException("perfis", "Perfil inválido: " + p);
                }
            }).toList();
            usuario.setPerfis(novosPerfis);
            if (usuario.getKeycloakId() != null) {
                try {
                    keycloakService.atualizarRoles(usuario.getKeycloakId(), dto.perfis());
                } catch (Exception e) {
                    LOG.warnf("Não foi possível atualizar roles no Keycloak para usuário id %d: %s", id, e.getMessage());
                }
            }
        }

        usuarioRepository.persist(usuario);
    }
}
