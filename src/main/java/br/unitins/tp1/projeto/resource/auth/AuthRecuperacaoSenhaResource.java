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

@Path("/auth/recuperar-senha")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthRecuperacaoSenhaResource {

    @Inject
    TokenRecuperacaoSenhaService service;

    @POST
    @Path("/solicitar")
    @Transactional
    public Response solicitar(@Valid SolicitarRecuperacaoRequestDTO dto) {
        service.solicitarRecuperacao(dto.login());
        return Response.ok().build();
    }

    @POST
    @Path("/redefinir")
    @Transactional
    public Response redefinir(@Valid RedefinirSenhaRequestDTO dto) {
        service.redefinirSenha(dto);
        return Response.noContent().build();
    }
}
