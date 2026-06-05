package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.EstadoRequestDTO;
import br.unitins.tp1.projeto.dto.EstadoResponseDTO;
import br.unitins.tp1.projeto.model.Estado;
import br.unitins.tp1.projeto.service.EstadoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/estados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Estados", description = "Operações relacionadas a estados")
public class EstadoResource {

    @Inject
    EstadoService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar estado", description = "Cria um novo estado")
    @APIResponse(responseCode = "201", description = "Estado criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid EstadoRequestDTO dto) {
        Estado estado = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(new EstadoResponseDTO(estado.getId(), estado.getNome(), estado.getSigla()))
                .build();
    }

    @GET
    @Operation(summary = "Listar estados", description = "Retorna a lista de todos os estados")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<EstadoResponseDTO> buscarTodos() {
        return service.findAll().stream()
                .map(e -> new EstadoResponseDTO(e.getId(), e.getNome(), e.getSigla()))
                .toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar estado por ID", description = "Retorna os detalhes de um estado específico")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Estado não encontrado")
    public EstadoResponseDTO encontrarPorId(@PathParam("id") Long id) {
        Estado estado = service.findById(id);
        if (estado == null) throw new NotFoundException("Estado não encontrado");
        return new EstadoResponseDTO(estado.getId(), estado.getNome(), estado.getSigla());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar estado", description = "Atualiza os dados de um estado existente")
    @APIResponse(responseCode = "204", description = "Estado atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Estado não encontrado")
    public Response atualizar(@PathParam("id") Long id, @Valid EstadoRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir estado", description = "Remove um estado pelo ID")
    @APIResponse(responseCode = "204", description = "Estado removido com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Estado não encontrado")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
