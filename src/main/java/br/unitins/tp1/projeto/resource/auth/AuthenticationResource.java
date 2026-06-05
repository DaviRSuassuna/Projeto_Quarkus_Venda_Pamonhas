package br.unitins.tp1.projeto.resource.auth;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.unitins.tp1.projeto.dto.auth.LoginRequestDTO;
import br.unitins.tp1.projeto.dto.auth.LoginResponseDTO;
import br.unitins.tp1.projeto.model.PessoaFisica;
import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.PessoaFisicaRepository;
import br.unitins.tp1.projeto.service.security.JwtService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.mindrot.jbcrypt.BCrypt;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticação", description = "Autenticação de usuários e geração de tokens JWT")
public class AuthenticationResource {

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Inject
    JwtService jwtService;

    @POST
    @Path("/login")
    @Operation(summary = "Login", description = "Autentica o usuário com e-mail e senha, retornando um token JWT para uso nas demais requisições")
    @APIResponse(responseCode = "200", description = "Login realizado com sucesso — token JWT retornado")
    @APIResponse(responseCode = "401", description = "Credenciais inválidas")
    public Response login(LoginRequestDTO request) {
        PessoaFisica pf = pessoaFisicaRepository.findByEmail(request.getEmail());
        if (pf == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        Usuario usuario = pf.getUsuario();

        if (!BCrypt.checkpw(request.getSenha(), usuario.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = jwtService.gerarToken(usuario);
        return Response.ok(new LoginResponseDTO(token)).build();
    }
}
