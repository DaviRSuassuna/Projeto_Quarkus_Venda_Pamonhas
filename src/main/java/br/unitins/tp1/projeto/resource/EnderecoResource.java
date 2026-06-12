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
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Endereços", description = "Operações relacionadas aos endereços do usuário autenticado")
public class EnderecoResource {

    @Inject
    EnderecoService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Listar endereços do usuário", description = "Retorna a lista paginada de endereços do usuário autenticado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public List<EnderecoResponseDTO> listar(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.listarPorUsuario(jwt.getName(), page, size);
    }

    @POST
    @RolesAllowed("ROLE_USER")
    @Transactional
    @Operation(summary = "Criar endereço", description = "Adiciona um novo endereço ao usuário autenticado")
    @APIResponse(responseCode = "201", description = "Endereço criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou perfil incompleto")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public Response criar(@Valid EnderecoRequestDTO dto) {
        EnderecoResponseDTO response = service.criar(jwt.getName(), dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    @Operation(summary = "Atualizar endereço", description = "Atualiza um endereço existente do usuário autenticado")
    @APIResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado — endereço pertence a outro usuário")
    @APIResponse(responseCode = "404", description = "Endereço não encontrado")
    public Response atualizar(@PathParam("id") Long id, @Valid EnderecoRequestDTO dto) {
        return Response.ok(service.atualizar(jwt.getName(), id, dto)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_USER")
    @Transactional
    @Operation(summary = "Excluir endereço", description = "Remove um endereço do usuário autenticado")
    @APIResponse(responseCode = "204", description = "Endereço removido com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado — endereço pertence a outro usuário")
    @APIResponse(responseCode = "404", description = "Endereço não encontrado")
    public Response deletar(@PathParam("id") Long id) {
        service.deletar(jwt.getName(), id);
        return Response.noContent().build();
    }
}
