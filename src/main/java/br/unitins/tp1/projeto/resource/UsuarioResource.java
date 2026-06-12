package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.AtualizarUsuarioAdminRequestDTO;
import br.unitins.tp1.projeto.dto.EditarUsuarioRequestDTO;
import br.unitins.tp1.projeto.dto.UsuarioResponseDTO;
import br.unitins.tp1.projeto.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
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

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários", description = "Operações relacionadas ao cadastro e gerenciamento de usuários")
public class UsuarioResource {

    @Inject
    UsuarioService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/me")
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Obter dados do usuário autenticado", description = "Retorna o perfil completo do usuário atualmente autenticado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public UsuarioResponseDTO buscarMe() {
        return service.buscarDadosLogado(jwt.getName());
    }

    @PUT
    @Path("/me")
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Editar dados do usuário autenticado", description = "Atualiza os dados pessoais do usuário autenticado")
    @APIResponse(responseCode = "204", description = "Dados atualizados com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public Response editarMe(@Valid EditarUsuarioRequestDTO dto) {
        service.editarDados(jwt.getName(), dto);
        return Response.noContent().build();
    }

    @GET
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Listar usuários (admin)", description = "Retorna a lista paginada de todos os usuários cadastrados")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public List<UsuarioResponseDTO> listarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.listarTodos(page, size);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar usuário (admin)", description = "Atualiza perfis e dados de um usuário pelo ID")
    @APIResponse(responseCode = "204", description = "Usuário atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response atualizarAdmin(@PathParam("id") Long id, @Valid AtualizarUsuarioAdminRequestDTO dto) {
        service.atualizarUsuarioAdmin(id, dto);
        return Response.noContent().build();
    }
}
