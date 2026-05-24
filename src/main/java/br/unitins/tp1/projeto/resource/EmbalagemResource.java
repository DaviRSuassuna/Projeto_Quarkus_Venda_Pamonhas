package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.dto.EmbalagemResponseDTO;
import br.unitins.tp1.projeto.mapper.EmbalagemMapper;
import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.service.EmbalagemService;
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

@Path("/embalagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmbalagemResource {

    @Inject
    EmbalagemService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response incluir(@Valid EmbalagemRequestDTO dto) {
        Embalagem embalagem = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(EmbalagemMapper.toResponseDTO(embalagem))
                .build();
    }

    @GET
    public List<EmbalagemResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public EmbalagemResponseDTO encontrarPorId(@PathParam("id") long id) {
        Embalagem embalagem = service.findById(id);
        if (embalagem == null) throw new NotFoundException("Embalagem não encontrada");
        return EmbalagemMapper.toResponseDTO(embalagem);
    }

    @GET
    @Path("/find/tipo/{tipo}")
    public List<EmbalagemResponseDTO> encontrarPorTipo(@PathParam("tipo") String tipo) {
        return service.findByTipo(tipo).stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/order/custo")
    public List<EmbalagemResponseDTO> encontrarOrdenadasPorCusto() {
        return service.findOrderByCusto().stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/biodegradavel/{dias}")
    public List<EmbalagemResponseDTO> encontrarBiodegradavelRapida(@PathParam("dias") int dias) {
        return service.findBiodegradavelRapida(dias).stream().map(EmbalagemMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response atualizar(@PathParam("id") Long id, @Valid EmbalagemRequestDTO dto) {
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
