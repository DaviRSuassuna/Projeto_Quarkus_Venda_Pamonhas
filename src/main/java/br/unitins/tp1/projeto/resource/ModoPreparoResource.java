package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoResponseDTO;
import br.unitins.tp1.projeto.mapper.ModoPreparoMapper;
import br.unitins.tp1.projeto.model.ModoPreparo;
import br.unitins.tp1.projeto.service.ModoPreparoService;
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

@Path("/modo-preparo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModoPreparoResource {

    @Inject
    ModoPreparoService service;

    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response incluir(@Valid ModoPreparoRequestDTO dto) {
        ModoPreparo modo = service.create(ModoPreparoMapper.toEntity(dto));
        return Response.status(Response.Status.CREATED)
                .entity(ModoPreparoMapper.toResponseDTO(modo))
                .build();
    }

    @GET
    public List<ModoPreparoResponseDTO> buscarTodos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return service.findAll(page, size).stream().map(ModoPreparoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public ModoPreparoResponseDTO encontrarPorId(@PathParam("id") long id) {
        ModoPreparo modo = service.findById(id);
        if (modo == null) throw new NotFoundException("ModoPreparo não encontrado");
        return ModoPreparoMapper.toResponseDTO(modo);
    }

    @GET
    @Path("/find/descricao/{descricao}")
    public List<ModoPreparoResponseDTO> encontrarPorDescricao(@PathParam("descricao") String descricao) {
        return service.findByDescricao(descricao).stream().map(ModoPreparoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/order/tempo")
    public List<ModoPreparoResponseDTO> encontrarOrdenadosPorTempo() {
        return service.findOrderByTempo().stream().map(ModoPreparoMapper::toResponseDTO).toList();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response atualizar(@PathParam("id") Long id, @Valid ModoPreparoRequestDTO dto) {
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
