package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.dto.CategoriaResponseDTO;
import br.unitins.tp1.projeto.mapper.CategoriaMapper;
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.service.CategoriaService;
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

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaResource {

    @Inject
    CategoriaService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response incluir(@Valid CategoriaRequestDTO dto) {
        Categoria categoria = service.create(CategoriaMapper.toEntity(dto));
        return Response.status(Response.Status.CREATED)
                .entity(CategoriaMapper.toResponseDTO(categoria))
                .build();
    }

    @GET
    public List<CategoriaResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(CategoriaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public CategoriaResponseDTO encontrarPorId(@PathParam("id") long id) {
        Categoria categoria = service.findById(id);
        if (categoria == null) throw new NotFoundException("Categoria não encontrada");
        return CategoriaMapper.toResponseDTO(categoria);
    }

    @GET
    @Path("/find/nome/{nome}")
    public List<CategoriaResponseDTO> encontrarPorNome(@PathParam("nome") String nome) {
        return service.findByNome(nome).stream().map(CategoriaMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/com-pamonhas")
    public List<CategoriaResponseDTO> encontrarComPamonhas() {
        return service.findComPamonhas().stream().map(CategoriaMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response atualizar(@PathParam("id") Long id, @Valid CategoriaRequestDTO dto) {
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
}
