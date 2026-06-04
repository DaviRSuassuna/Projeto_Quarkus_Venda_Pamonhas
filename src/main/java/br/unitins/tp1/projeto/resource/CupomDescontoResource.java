package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.dto.CupomDescontoResponseDTO;
import br.unitins.tp1.projeto.service.CupomDescontoService;
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

@Path("/cupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CupomDescontoResource {

    @Inject
    CupomDescontoService service;

    @POST
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Response criar(@Valid CupomDescontoRequestDTO dto) {
        CupomDescontoResponseDTO response = service.criar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @RolesAllowed("ROLE_ADMIN")
    public List<CupomDescontoResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GET
    @Path("/validar/{codigo}")
    @RolesAllowed("ROLE_USER")
    public CupomDescontoResponseDTO validar(@PathParam("codigo") String codigo) {
        return service.validarCodigo(codigo);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        service.deletar(id);
        return Response.noContent().build();
    }
}
