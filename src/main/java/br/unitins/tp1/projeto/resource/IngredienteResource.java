package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.dto.IngredienteResponseDTO;
import br.unitins.tp1.projeto.mapper.IngredienteMapper;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.service.IngredienteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/ingredientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Ingredientes", description = "Operações relacionadas a ingredientes e controle de estoque")
public class IngredienteResource {

    @Inject
    IngredienteService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar ingrediente", description = "Cria um novo ingrediente com quantidade em estoque e unidade de medida")
    @APIResponse(responseCode = "201", description = "Ingrediente criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou nome duplicado")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid IngredienteRequestDTO dto) {
        Ingrediente ingrediente = service.create(IngredienteMapper.toEntity(dto));
        return Response.status(Response.Status.CREATED)
                .entity(IngredienteMapper.toResponseDTO(ingrediente))
                .build();
    }

    @GET
    @Operation(summary = "Listar ingredientes", description = "Retorna a lista paginada de todos os ingredientes cadastrados")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<IngredienteResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(IngredienteMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar ingrediente por ID", description = "Retorna os detalhes de um ingrediente específico")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Ingrediente não encontrado")
    public IngredienteResponseDTO encontrarPorId(@PathParam("id") long id) {
        Ingrediente ingrediente = service.findById(id);
        if (ingrediente == null) throw new NotFoundException("Ingrediente não encontrado");
        return IngredienteMapper.toResponseDTO(ingrediente);
    }

    @GET
    @Path("/find/estoque-abaixo/{estoque-abaixo}")
    @Operation(summary = "Buscar ingredientes com estoque abaixo do limite", description = "Retorna ingredientes cuja quantidade em estoque é inferior ao valor informado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<IngredienteResponseDTO> encontrarPorEstoqueAbaixo(@PathParam("estoque-abaixo") Double qtdEstoque) {
        return service.findByEstoqueAbaixo(qtdEstoque).stream().map(i -> IngredienteMapper.toResponseDTO(i)).toList();
    }

    @GET
    @Path("/find/unidade-medida/{unidade-medida}")
    @Operation(summary = "Buscar ingredientes por unidade de medida", description = "Retorna ingredientes filtrados pela unidade de medida informada")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<IngredienteResponseDTO> encontrarPorUnidadeMedida(@PathParam("unidade-medida") String unidadeMedida) {
        return service.findByUnidadeMedida(unidadeMedida).stream().map(i -> IngredienteMapper.toResponseDTO(i)).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar ingrediente", description = "Atualiza os dados de um ingrediente existente")
    @APIResponse(responseCode = "204", description = "Ingrediente atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Ingrediente não encontrado")
    public Response atualizar(@PathParam("id") Long id, @Valid IngredienteRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir ingrediente", description = "Remove um ingrediente pelo ID")
    @APIResponse(responseCode = "204", description = "Ingrediente removido com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Ingrediente não encontrado")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
