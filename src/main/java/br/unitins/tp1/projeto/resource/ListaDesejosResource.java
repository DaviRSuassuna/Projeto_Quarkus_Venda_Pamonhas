package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.dto.ListaDesejosResponseDTO;
import br.unitins.tp1.projeto.service.ListaDesejosService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

@Path("/lista-desejos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListaDesejosResource {

    @Inject
    ListaDesejosService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("ROLE_USER")
    public ListaDesejosResponseDTO buscar() {
        return service.buscarPorUsuario(jwt.getName());
    }

    @POST
    @Path("/{pamonhaId}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    public Response adicionar(@PathParam("pamonhaId") Long pamonhaId) {
        service.adicionarPamonha(jwt.getName(), pamonhaId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{pamonhaId}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    public Response remover(@PathParam("pamonhaId") Long pamonhaId) {
        service.removerPamonha(jwt.getName(), pamonhaId);
        return Response.noContent().build();
    }
}
