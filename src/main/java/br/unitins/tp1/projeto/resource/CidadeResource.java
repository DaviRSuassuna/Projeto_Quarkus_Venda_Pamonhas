package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CidadeRequestDTO;
import br.unitins.tp1.projeto.dto.CidadeResponseDTO;
import br.unitins.tp1.projeto.model.Cidade;
import br.unitins.tp1.projeto.service.CidadeService;
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

@Path("/cidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cidades", description = "Operações relacionadas a cidades")
public class CidadeResource {

    @Inject
    CidadeService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar cidade", description = "Cria uma nova cidade")
    @APIResponse(responseCode = "201", description = "Cidade criada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou estado não encontrado")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid CidadeRequestDTO dto) {
        Cidade cidade = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(new CidadeResponseDTO(cidade.getId(), cidade.getNome(), cidade.getEstado().getNome()))
                .build();
    }

    @GET
    @Operation(summary = "Listar cidades", description = "Retorna a lista de todas as cidades")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<CidadeResponseDTO> buscarTodos() {
        return service.findAll().stream()
                .map(c -> new CidadeResponseDTO(c.getId(), c.getNome(), c.getEstado().getNome()))
                .toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar cidade por ID", description = "Retorna os detalhes de uma cidade específica")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Cidade não encontrada")
    public CidadeResponseDTO encontrarPorId(@PathParam("id") Long id) {
        Cidade cidade = service.findById(id);
        if (cidade == null) throw new NotFoundException("Cidade não encontrada");
        return new CidadeResponseDTO(cidade.getId(), cidade.getNome(), cidade.getEstado().getNome());
    }

    @GET
    @Path("/estado/{estadoId}")
    @Operation(summary = "Listar cidades por estado", description = "Retorna cidades de um estado específico")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<CidadeResponseDTO> encontrarPorEstado(@PathParam("estadoId") Long estadoId) {
        return service.findByEstado(estadoId).stream()
                .map(c -> new CidadeResponseDTO(c.getId(), c.getNome(), c.getEstado().getNome()))
                .toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar cidade", description = "Atualiza os dados de uma cidade existente")
    @APIResponse(responseCode = "204", description = "Cidade atualizada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou estado não encontrado")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Cidade não encontrada")
    public Response atualizar(@PathParam("id") Long id, @Valid CidadeRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir cidade", description = "Remove uma cidade pelo ID")
    @APIResponse(responseCode = "204", description = "Cidade removida com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Cidade não encontrada")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
