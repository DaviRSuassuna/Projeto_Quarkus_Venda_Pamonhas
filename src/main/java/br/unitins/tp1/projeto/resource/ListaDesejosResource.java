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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/lista-desejos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Lista de Desejos", description = "Operações relacionadas à lista de desejos do usuário autenticado")
public class ListaDesejosResource {

    @Inject
    ListaDesejosService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Buscar lista de desejos", description = "Retorna a lista de desejos do usuário autenticado com os detalhes das pamonhas")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public ListaDesejosResponseDTO buscar() {
        return service.buscarPorUsuario(jwt.getName());
    }

    @POST
    @Path("/{pamonhaId}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    @Operation(summary = "Adicionar pamonha à lista de desejos", description = "Adiciona uma pamonha à lista de desejos do usuário autenticado")
    @APIResponse(responseCode = "204", description = "Pamonha adicionada com sucesso")
    @APIResponse(responseCode = "400", description = "Pamonha já está na lista de desejos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "404", description = "Pamonha não encontrada")
    public Response adicionar(@PathParam("pamonhaId") Long pamonhaId) {
        service.adicionarPamonha(jwt.getName(), pamonhaId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{pamonhaId}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    @Operation(summary = "Remover pamonha da lista de desejos", description = "Remove uma pamonha da lista de desejos do usuário autenticado")
    @APIResponse(responseCode = "204", description = "Pamonha removida com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "404", description = "Pamonha não encontrada na lista de desejos")
    public Response remover(@PathParam("pamonhaId") Long pamonhaId) {
        service.removerPamonha(jwt.getName(), pamonhaId);
        return Response.noContent().build();
    }
}
