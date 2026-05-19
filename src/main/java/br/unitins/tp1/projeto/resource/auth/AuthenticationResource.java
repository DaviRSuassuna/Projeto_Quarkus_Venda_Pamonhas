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
import br.unitins.tp1.projeto.model.Usuario;
import br.unitins.tp1.projeto.repository.UsuarioRepository;
import br.unitins.tp1.projeto.service.security.JwtService;
import org.mindrot.jbcrypt.BCrypt;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    JwtService jwtService;

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.find("login", request.getLogin()).firstResult();
        if (usuario == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();

        if (!BCrypt.checkpw(request.getSenha(), usuario.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = jwtService.gerarToken(usuario);
        return Response.ok(new LoginResponseDTO(token)).build();
    }
}
