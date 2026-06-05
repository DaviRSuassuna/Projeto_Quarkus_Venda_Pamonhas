package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.dto.AlterarSenhaRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroCompletoRequestDTO;
import br.unitins.tp1.projeto.dto.CadastroSimplesRequestDTO;
import br.unitins.tp1.projeto.dto.EditarUsuarioRequestDTO;
import br.unitins.tp1.projeto.dto.UsuarioResponseDTO;
import br.unitins.tp1.projeto.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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

    @POST
    @Path("/cadastro-simples")
    @Operation(summary = "Cadastro simples", description = "Registra um novo usuário apenas com e-mail e senha, sem dados pessoais completos")
    @APIResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
    public Response cadastrarSimples(@Valid CadastroSimplesRequestDTO dto) {
        service.cadastrarSimples(dto);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/cadastro-completo")
    @Operation(summary = "Cadastro completo", description = "Registra um novo usuário com todos os dados pessoais (nome, CPF, data de nascimento, telefone)")
    @APIResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou e-mail/CPF já cadastrado")
    public Response cadastrarCompleto(@Valid CadastroCompletoRequestDTO dto) {
        service.cadastrarCompleto(dto);
        return Response.status(Response.Status.CREATED).build();
    }

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

    @PUT
    @Path("/me/senha")
    @RolesAllowed("ROLE_USER")
    @Operation(summary = "Alterar senha do usuário autenticado", description = "Altera a senha do usuário autenticado mediante confirmação da senha atual")
    @APIResponse(responseCode = "204", description = "Senha alterada com sucesso")
    @APIResponse(responseCode = "400", description = "Senha atual incorreta ou nova senha inválida")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    public Response alterarSenha(@Valid AlterarSenhaRequestDTO dto) {
        service.alterarSenha(jwt.getName(), dto);
        return Response.noContent().build();
    }
}
