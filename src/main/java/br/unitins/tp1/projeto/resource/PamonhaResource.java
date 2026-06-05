package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaResponseDTO;
import br.unitins.tp1.projeto.dto.PamonhaEcommerceResponseDTO;
import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.mapper.PamonhaMapper;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
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

@Path("/pamonhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Pamonhas", description = "Operações relacionadas a pamonhas do e-commerce")
public class PamonhaResource {

    @Inject
    PamonhaService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Cadastrar pamonha", description = "Cria uma nova pamonha com receita, embalagem, modo de preparo e tabela nutricional")
    @APIResponse(responseCode = "201", description = "Pamonha cadastrada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    public Response incluir(@Valid PamonhaRequestDTO dto) {
        PamonhaResponseDTO response = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Operation(summary = "Listar pamonhas", description = "Retorna a lista paginada de todas as pamonhas cadastradas")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<PamonhaResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(PamonhaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar pamonha por ID", description = "Retorna os detalhes completos de uma pamonha específica")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Pamonha não encontrada")
    public PamonhaResponseDTO encontrarPorId(@PathParam("id") long id) {
        Pamonha pamonha = service.findById(id);
        if (pamonha == null) throw new NotFoundException("Pamonha não encontrada");
        return PamonhaMapper.toResponseDTO(pamonha);
    }

    @GET
    @Path("/find/nome/{nome}")
    @Operation(summary = "Buscar pamonhas por nome", description = "Retorna pamonhas cujo nome contenha o texto informado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<PamonhaResponseDTO> encontrarPorNome(@PathParam("nome") String nome) {
        return service.findByNome(nome).stream().map(PamonhaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/categoria/{categoriaId}")
    @Operation(summary = "Buscar pamonhas por categoria", description = "Retorna pamonhas pertencentes à categoria informada")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<PamonhaResponseDTO> encontrarPorCategoria(@PathParam("categoriaId") Long categoriaId) {
        return service.findByCategoria(categoriaId).stream().map(PamonhaMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Atualizar pamonha", description = "Atualiza os dados de uma pamonha existente")
    @APIResponse(responseCode = "204", description = "Pamonha atualizada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Pamonha não encontrada")
    public Response atualizar(@PathParam("id") Long id, @Valid PamonhaRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(summary = "Excluir pamonha", description = "Remove uma pamonha pelo ID")
    @APIResponse(responseCode = "204", description = "Pamonha removida com sucesso")
    @APIResponse(responseCode = "401", description = "Não autenticado")
    @APIResponse(responseCode = "403", description = "Acesso negado")
    @APIResponse(responseCode = "404", description = "Pamonha não encontrada")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/ecommerce")
    @Operation(summary = "Listar pamonhas para o e-commerce", description = "Retorna a lista paginada de pamonhas com dados simplificados para exibição no e-commerce")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<PamonhaEcommerceResponseDTO> buscarParaEcommerce(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream()
                .map(p -> new PamonhaEcommerceResponseDTO(
                    p.getId(), p.getNome(), p.getDescricao(),
                    p.getPreco(), p.getEstoque(),
                    p.getCategorias().stream()
                        .map(c -> new CategoriaResponseDTO(c.getId(), c.getNome(), c.getDescricao()))
                        .toList()
                ))
                .toList();
    }

    @GET
    @Path("/ecommerce/{id}")
    @Operation(summary = "Buscar pamonha para o e-commerce por ID", description = "Retorna dados simplificados de uma pamonha específica para exibição no e-commerce")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Pamonha não encontrada")
    public PamonhaEcommerceResponseDTO buscarPorIdParaEcommerce(@PathParam("id") Long id) {
        Pamonha p = service.findById(id);
        if (p == null) throw new NotFoundException("Pamonha não encontrada");
        return new PamonhaEcommerceResponseDTO(
            p.getId(), p.getNome(), p.getDescricao(),
            p.getPreco(), p.getEstoque(),
            p.getCategorias().stream()
                .map(c -> new CategoriaResponseDTO(c.getId(), c.getNome(), c.getDescricao()))
                .toList()
        );
    }
}
