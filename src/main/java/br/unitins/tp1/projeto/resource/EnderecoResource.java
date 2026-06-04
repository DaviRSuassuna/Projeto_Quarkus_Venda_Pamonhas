package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.EnderecoRequestDTO;
import br.unitins.tp1.projeto.dto.EnderecoResponseDTO;
import br.unitins.tp1.projeto.service.EnderecoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    @Inject
    EnderecoService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("ROLE_USER")
    public List<EnderecoResponseDTO> listar() {
        return service.listarPorUsuario(jwt.getName());
    }

    @POST
    @RolesAllowed("ROLE_USER")
    @Transactional
    public Response criar(@Valid EnderecoRequestDTO dto) {
        EnderecoResponseDTO response = service.criar(jwt.getName(), dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        service.deletar(jwt.getName(), id);
        return Response.noContent().build();
    }
}
