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

@Path("/pamonhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PamonhaResource {

    @Inject
    PamonhaService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response incluir(@Valid PamonhaRequestDTO dto) {
        PamonhaResponseDTO response = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public List<PamonhaResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(PamonhaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public PamonhaResponseDTO encontrarPorId(@PathParam("id") long id) {
        Pamonha pamonha = service.findById(id);
        if (pamonha == null) throw new NotFoundException("Pamonha não encontrada");
        return PamonhaMapper.toResponseDTO(pamonha);
    }

    @GET
    @Path("/find/nome/{nome}")
    public List<PamonhaResponseDTO> encontrarPorNome(@PathParam("nome") String nome) {
        return service.findByNome(nome).stream().map(PamonhaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/categoria/{categoriaId}")
    public List<PamonhaResponseDTO> encontrarPorCategoria(@PathParam("categoriaId") Long categoriaId) {
        return service.findByCategoria(categoriaId).stream().map(PamonhaMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response atualizar(@PathParam("id") Long id, @Valid PamonhaRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/ecommerce")
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

