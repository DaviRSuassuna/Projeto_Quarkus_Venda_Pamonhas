package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.dto.AlterarSenhaRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroCompletoRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroSimplesRequestDTO;
import br.unitins.tp1.projeto.dto.EditarUsuarioRequestDTO;
import br.unitins.tp1.projeto.dto.UsuarioResponseDTO;
import br.unitins.tp1.projeto.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/cadastro-simples")
    public Response cadastrarSimples(@Valid CadastroSimplesRequestDTO dto) {
        service.cadastrarSimples(dto);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/cadastro-completo")
    public Response cadastrarCompleto(@Valid CadastroCompletoRequestDTO dto) {
        service.cadastrarCompleto(dto);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed("ROLE_USER")
    public UsuarioResponseDTO buscarMe() {
        return service.buscarDadosLogado(jwt.getName());
    }

    @PUT
    @Path("/me")
    @RolesAllowed("ROLE_USER")
    public Response editarMe(@Valid EditarUsuarioRequestDTO dto) {
        service.editarDados(jwt.getName(), dto);
        return Response.noContent().build();
    }

    @PUT
    @Path("/me/senha")
    @RolesAllowed("ROLE_USER")
    public Response alterarSenha(@Valid AlterarSenhaRequestDTO dto) {
        service.alterarSenha(jwt.getName(), dto);
        return Response.noContent().build();
    }
}
