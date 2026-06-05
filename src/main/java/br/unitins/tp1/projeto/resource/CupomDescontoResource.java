package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.dto.CupomDescontoResponseDTO;
import br.unitins.tp1.projeto.service.CupomDescontoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/cupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cupons de Desconto", description = "Operações relacionadas a cupons de desconto")
public class CupomDescontoResource {

    @Inject
    CupomDescontoService service;

    @POST
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    @Operation(summary = "Criar cupom de desconto", description = "Cria um novo cupom de desconto associado a pamonhas específicas")
    @APIResponse(responseCode = "201", description = "Cupom criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response criar(@Valid CupomDescontoRequestDTO dto) {
        CupomDescontoResponseDTO response = service.criar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Listar cupons de desconto", description = "Retorna a lista paginada de todos os cupons de desconto")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public List<CupomDescontoResponseDTO> listarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.listarTodos(page, size);
    }

    @GET
    @Path("/validar/{codigo}")
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Validar cupom por código", description = "Valida se um cupom de desconto é válido e não está expirado")
    @APIResponse(responseCode = "200", description = "Cupom válido")
    @APIResponse(responseCode = "400", description = "Cupom inválido ou expirado")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public CupomDescontoResponseDTO validar(@PathParam("codigo") String codigo) {
        return service.validarCodigo(codigo);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Transactional
    @Operation(summary = "Excluir cupom de desconto", description = "Remove um cupom de desconto pelo ID")
    @APIResponse(responseCode = "204", description = "Cupom removido com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Cupom não encontrado")
    public Response deletar(@PathParam("id") Long id) {
        service.deletar(id);
        return Response.noContent().build();
    }
}
