package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoResponseDTO;
import br.unitins.tp1.projeto.mapper.ModoPreparoMapper;
import br.unitins.tp1.projeto.model.ModoPreparo;
import br.unitins.tp1.projeto.service.ModoPreparoService;
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

@Path("/modo-preparo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Modos de Preparo", description = "Operações relacionadas a modos de preparo de pamonhas")
public class ModoPreparoResource {

    @Inject
    ModoPreparoService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar modo de preparo", description = "Cria um novo modo de preparo com descrição e tempo de preparo")
    @APIResponse(responseCode = "201", description = "Modo de preparo criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou descrição duplicada")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid ModoPreparoRequestDTO dto) {
        ModoPreparo modo = service.create(ModoPreparoMapper.toEntity(dto));
        return Response.status(Response.Status.CREATED)
                .entity(ModoPreparoMapper.toResponseDTO(modo))
                .build();
    }

    @GET
    @Operation(summary = "Listar modos de preparo", description = "Retorna a lista paginada de todos os modos de preparo cadastrados")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<ModoPreparoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(ModoPreparoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar modo de preparo por ID", description = "Retorna os detalhes de um modo de preparo específico")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Modo de preparo não encontrado")
    public ModoPreparoResponseDTO encontrarPorId(@PathParam("id") long id) {
        ModoPreparo modo = service.findById(id);
        if (modo == null) throw new NotFoundException("ModoPreparo não encontrado");
        return ModoPreparoMapper.toResponseDTO(modo);
    }

    @GET
    @Path("/find/descricao/{descricao}")
    @Operation(summary = "Buscar modos de preparo por descrição", description = "Retorna modos de preparo cuja descrição contenha o texto informado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<ModoPreparoResponseDTO> encontrarPorDescricao(@PathParam("descricao") String descricao) {
        return service.findByDescricao(descricao).stream().map(ModoPreparoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/order/tempo")
    @Operation(summary = "Listar modos de preparo ordenados por tempo", description = "Retorna todos os modos de preparo ordenados pelo tempo de preparo em ordem crescente")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<ModoPreparoResponseDTO> encontrarOrdenadosPorTempo() {
        return service.findOrderByTempo().stream().map(ModoPreparoMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar modo de preparo", description = "Atualiza os dados de um modo de preparo existente")
    @APIResponse(responseCode = "204", description = "Modo de preparo atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Modo de preparo não encontrado")
    public Response atualizar(@PathParam("id") Long id, @Valid ModoPreparoRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir modo de preparo", description = "Remove um modo de preparo pelo ID")
    @APIResponse(responseCode = "204", description = "Modo de preparo removido com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Modo de preparo não encontrado")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
