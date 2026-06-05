package br.unitins.tp1.projeto.resource.auth;

import br.unitins.tp1.projeto.dto.RedefinirSenhaRequestDTO;
import br.unitins.tp1.projeto.dto.SolicitarRecuperacaoRequestDTO;
import br.unitins.tp1.projeto.service.TokenRecuperacaoSenhaService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth/recuperar-senha")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Recuperação de Senha", description = "Fluxo de solicitação e redefinição de senha por e-mail")
public class AuthRecuperacaoSenhaResource {

    @Inject
    TokenRecuperacaoSenhaService service;

    @POST
    @Path("/solicitar")
    @Transactional
    @Operation(summary = "Solicitar recuperação de senha", description = "Envia um e-mail com token de recuperação para o endereço informado (token válido por 2 horas)")
    @APIResponse(responseCode = "200", description = "Solicitação enviada — e-mail de recuperação encaminhado")
    @APIResponse(responseCode = "400", description = "E-mail inválido ou não cadastrado")
    public Response solicitar(@Valid SolicitarRecuperacaoRequestDTO dto) {
        service.solicitarRecuperacao(dto.email());
        return Response.ok().build();
    }

    @POST
    @Path("/redefinir")
    @Transactional
    @Operation(summary = "Redefinir senha", description = "Redefine a senha do usuário utilizando o token recebido por e-mail")
    @APIResponse(responseCode = "204", description = "Senha redefinida com sucesso")
    @APIResponse(responseCode = "400", description = "Token inválido, expirado ou nova senha inválida")
    public Response redefinir(@Valid RedefinirSenhaRequestDTO dto) {
        service.redefinirSenha(dto);
        return Response.noContent().build();
    }
}
