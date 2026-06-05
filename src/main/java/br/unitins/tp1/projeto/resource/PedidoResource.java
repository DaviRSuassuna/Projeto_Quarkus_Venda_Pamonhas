package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.AtualizarStatusPedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
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

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos do e-commerce")
public class PedidoResource {

    @Inject
    PedidoService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed("ROLE_USER")
    @Transactional
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido para o usuário autenticado com itens, endereço de entrega e forma de pagamento")
    @APIResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public Response criar(@Valid PedidoRequestDTO dto) {
        PedidoResponseDTO response = service.criar(jwt.getName(), dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Listar pedidos do usuário", description = "Retorna a lista paginada de pedidos do usuário autenticado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public List<PedidoResponseDTO> listar(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.listarPorUsuario(jwt.getName(), page, size);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido específico do usuário autenticado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado — pedido pertence a outro usuário")
    @APIResponse(responseCode = "404", description = "Pedido não encontrado")
    public PedidoResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(jwt.getName(), id);
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido (somente administradores)")
    @APIResponse(responseCode = "204", description = "Status atualizado com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Pedido não encontrado")
    public Response atualizarStatus(@PathParam("id") Long id, @Valid AtualizarStatusPedidoRequestDTO dto) {
        service.atualizarStatus(id, dto.status());
        return Response.noContent().build();
    }
}
