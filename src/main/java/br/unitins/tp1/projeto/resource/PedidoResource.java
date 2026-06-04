package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.AtualizarStatusPedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed("ROLE_USER")
    @Transactional
    public Response criar(@Valid PedidoRequestDTO dto) {
        PedidoResponseDTO response = service.criar(jwt.getName(), dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @RolesAllowed("ROLE_USER")
    public List<PedidoResponseDTO> listar() {
        return service.listarPorUsuario(jwt.getName());
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ROLE_USER")
    public PedidoResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(jwt.getName(), id);
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Response atualizarStatus(@PathParam("id") Long id, @Valid AtualizarStatusPedidoRequestDTO dto) {
        service.atualizarStatus(id, dto.status());
        return Response.noContent().build();
    }
}
