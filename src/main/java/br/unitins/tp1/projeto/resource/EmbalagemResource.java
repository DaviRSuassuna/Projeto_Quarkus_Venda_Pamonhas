package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.dto.EmbalagemResponseDTO;
import br.unitins.tp1.projeto.mapper.EmbalagemMapper;
import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.service.EmbalagemService;
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

@Path("/embalagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Embalagens", description = "Operações relacionadas a embalagens (plásticas e biodegradáveis)")
public class EmbalagemResource {

    @Inject
    EmbalagemService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar embalagem", description = "Cria uma nova embalagem plástica ou biodegradável")
    @APIResponse(responseCode = "201", description = "Embalagem criada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid EmbalagemRequestDTO dto) {
        Embalagem embalagem = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(EmbalagemMapper.toResponseDTO(embalagem))
                .build();
    }

    @GET
    @Operation(summary = "Listar embalagens", description = "Retorna a lista paginada de todas as embalagens cadastradas")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<EmbalagemResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar embalagem por ID", description = "Retorna os detalhes de uma embalagem específica")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Embalagem não encontrada")
    public EmbalagemResponseDTO encontrarPorId(@PathParam("id") long id) {
        Embalagem embalagem = service.findById(id);
        if (embalagem == null) throw new NotFoundException("Embalagem não encontrada");
        return EmbalagemMapper.toResponseDTO(embalagem);
    }

    @GET
    @Path("/find/tipo/{tipo}")
    @Operation(summary = "Buscar embalagens por tipo", description = "Retorna embalagens do tipo informado (PLASTICA ou BIODEGRADAVEL)")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<EmbalagemResponseDTO> encontrarPorTipo(@PathParam("tipo") String tipo) {
        return service.findByTipo(tipo).stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/order/custo")
    @Operation(summary = "Listar embalagens ordenadas por custo", description = "Retorna todas as embalagens ordenadas pelo custo em ordem crescente")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<EmbalagemResponseDTO> encontrarOrdenadasPorCusto() {
        return service.findOrderByCusto().stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/biodegradavel/{dias}")
    @Operation(summary = "Buscar embalagens biodegradáveis por tempo de decomposição", description = "Retorna embalagens biodegradáveis com tempo de decomposição inferior ao valor informado (em dias)")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<EmbalagemResponseDTO> encontrarBiodegradavelRapida(@PathParam("dias") int dias) {
        return service.findBiodegradavelRapida(dias).stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar embalagem", description = "Atualiza os dados de uma embalagem existente (o tipo não pode ser alterado)")
    @APIResponse(responseCode = "204", description = "Embalagem atualizada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Embalagem não encontrada")
    public Response atualizar(@PathParam("id") Long id, @Valid EmbalagemRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir embalagem", description = "Remove uma embalagem pelo ID")
    @APIResponse(responseCode = "204", description = "Embalagem removida com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Embalagem não encontrada")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
