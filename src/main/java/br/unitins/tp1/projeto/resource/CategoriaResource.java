package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.dto.CategoriaResponseDTO;
import br.unitins.tp1.projeto.mapper.CategoriaMapper;
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.service.CategoriaService;
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

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Categorias", description = "Operações relacionadas a categorias de pamonhas")
public class CategoriaResource {

    @Inject
    CategoriaService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar categoria", description = "Cria uma nova categoria de pamonhas")
    @APIResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou nome duplicado")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid CategoriaRequestDTO dto) {
        Categoria categoria = service.create(CategoriaMapper.toEntity(dto));
        return Response.status(Response.Status.CREATED)
                .entity(CategoriaMapper.toResponseDTO(categoria))
                .build();
    }

    @GET
    @Operation(summary = "Listar categorias", description = "Retorna a lista paginada de todas as categorias")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<CategoriaResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(CategoriaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna os detalhes de uma categoria específica")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Categoria não encontrada")
    public CategoriaResponseDTO encontrarPorId(@PathParam("id") long id) {
        Categoria categoria = service.findById(id);
        if (categoria == null) throw new NotFoundException("Categoria não encontrada");
        return CategoriaMapper.toResponseDTO(categoria);
    }

    @GET
    @Path("/find/nome/{nome}")
    @Operation(summary = "Buscar categorias por nome", description = "Retorna categorias cujo nome contenha o texto informado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<CategoriaResponseDTO> encontrarPorNome(@PathParam("nome") String nome) {
        return service.findByNome(nome).stream().map(CategoriaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/com-pamonhas")
    @Operation(summary = "Listar categorias com pamonhas", description = "Retorna somente as categorias que possuem ao menos uma pamonha associada")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<CategoriaResponseDTO> encontrarComPamonhas() {
        return service.findComPamonhas().stream().map(CategoriaMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    @APIResponse(responseCode = "204", description = "Categoria atualizada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou nome duplicado")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Categoria não encontrada")
    public Response atualizar(@PathParam("id") Long id, @Valid CategoriaRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria pelo ID")
    @APIResponse(responseCode = "204", description = "Categoria removida com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Categoria não encontrada")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
