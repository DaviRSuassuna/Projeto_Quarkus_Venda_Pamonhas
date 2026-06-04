package br.unitins.tp1.projeto.service;

import java.util.List;

import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import br.unitins.tp1.projeto.dto.AlterarSenhaRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroCompletoRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroSimplesRequestDTO;
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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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

    @Override
    @Transactional
    public void cadastrarSimples(CadastroSimplesRequestDTO dto) {
        if (usuarioRepository.find("login", dto.login()).firstResult() != null) {
            throw new ValidationException("login", "Login já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(BCrypt.hashpw(dto.senha(), BCrypt.gensalt()));
        usuario.setPerfis(List.of(Perfil.ROLE_USER));
        usuarioRepository.persist(usuario);

        try {
            String keycloakId = keycloakService.registrarUsuario(dto.login(), dto.senha(), "ROLE_USER");
            if (keycloakId != null) {
                usuario.setKeycloakId(keycloakId);
            }
        } catch (Exception e) {
            LOG.warnf("Não foi possível sincronizar usuário '%s' com o Keycloak: %s", dto.login(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void cadastrarCompleto(CadastroCompletoRequestDTO dto) {
        if (usuarioRepository.find("login", dto.login()).firstResult() != null) {
            throw new ValidationException("login", "Login já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(BCrypt.hashpw(dto.senha(), BCrypt.gensalt()));
        usuario.setPerfis(List.of(Perfil.ROLE_USER));
        usuarioRepository.persist(usuario);

        PessoaFisica pf = new PessoaFisica();
        pf.setNome(dto.nome());
        pf.setSobrenome(dto.sobrenome());
        pf.setCpf(dto.cpf());
        pf.setEmail(dto.email());
        pf.setTelefone(dto.telefone());
        pf.setDataNascimento(dto.dataNascimento());
        pf.setUsuario(usuario);
        pessoaFisicaRepository.persist(pf);

        ListaDesejos lista = new ListaDesejos();
        lista.setUsuario(usuario);
        listaDesejosRepository.persist(lista);

        try {
            String keycloakId = keycloakService.registrarUsuario(dto.login(), dto.senha(), "ROLE_USER");
            if (keycloakId != null) {
                usuario.setKeycloakId(keycloakId);
            }
        } catch (Exception e) {
            LOG.warnf("Não foi possível sincronizar usuário '%s' com o Keycloak: %s", dto.login(), e.getMessage());
        }
    }

    @Override
    public UsuarioResponseDTO buscarDadosLogado(String login) {
        Usuario usuario = usuarioRepository.find("login", login).firstResult();
        if (usuario == null) throw new jakarta.ws.rs.NotFoundException("Usuário não encontrado");
        PessoaFisica pf = pessoaFisicaRepository.findByUsuarioLogin(login);
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getLogin(),
            pf != null ? pf.getNome() : null,
            pf != null ? pf.getEmail() : null
        );
    }

    @Override
    @Transactional
    public void editarDados(String login, EditarUsuarioRequestDTO dto) {
        PessoaFisica pf = pessoaFisicaRepository.findByUsuarioLogin(login);
        if (pf == null) throw new jakarta.ws.rs.NotFoundException("Perfil completo não encontrado");
        if (dto.nome() != null) pf.setNome(dto.nome());
        if (dto.email() != null) pf.setEmail(dto.email());
        if (dto.telefone() != null) pf.setTelefone(dto.telefone());
        pessoaFisicaRepository.persist(pf);
    }

    @Override
    @Transactional
    public void alterarSenha(String login, AlterarSenhaRequestDTO dto) {
        Usuario usuario = usuarioRepository.find("login", login).firstResult();
        if (usuario == null) throw new jakarta.ws.rs.NotFoundException("Usuário não encontrado");
        if (!BCrypt.checkpw(dto.senhaAtual(), usuario.getSenha())) {
            throw new ValidationException("senhaAtual", "Senha incorreta");
        }
        usuario.setSenha(BCrypt.hashpw(dto.novaSenha(), BCrypt.gensalt()));
        usuarioRepository.persist(usuario);

        try {
            if (usuario.getKeycloakId() != null) {
                keycloakService.atualizarSenha(usuario.getKeycloakId(), dto.novaSenha());
            }
        } catch (Exception e) {
            LOG.warnf("Não foi possível atualizar senha no Keycloak para '%s': %s", login, e.getMessage());
        }
    }
}
