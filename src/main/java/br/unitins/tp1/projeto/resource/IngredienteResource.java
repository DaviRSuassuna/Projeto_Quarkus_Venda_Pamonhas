package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.dto.IngredienteResponseDTO;
import br.unitins.tp1.projeto.mapper.IngredienteMapper;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.service.IngredienteService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ingredientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IngredienteResource {

    @Inject
    IngredienteService service;

    @POST
    @Transactional
    public Response incluir(@Valid IngredienteRequestDTO dto) {
        Ingrediente ingrediente = service.create(IngredienteMapper.toEntity(dto));
        return Response.status(Response.Status.CREATED)
                .entity(IngredienteMapper.toResponseDTO(ingrediente))
                .build();
    }

    @GET
    public List<IngredienteResponseDTO> buscarTodos() {
        return service.findAll().stream().map(i -> IngredienteMapper.toResponseDTO(i)).toList();
    }

    @GET
    @Path("/{id}")
    public IngredienteResponseDTO encontrarPorId(@PathParam("id") long id) {
        return IngredienteMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/estoque-abaixo/{estoque-abaixo}")
    public List<IngredienteResponseDTO> encontrarPorEstoqueAbaixo(@PathParam("estoque-abaixo") Double qtdEstoque ) {
        return service.findByEstoqueAbaixo(qtdEstoque).stream().map(i -> IngredienteMapper.toResponseDTO(i)).toList();
    }

    @GET
    @Path("/find/unidade-medida/{unidade-medida}")
    public List<IngredienteResponseDTO> encontrarPorUnidadeMedida(@PathParam("unidade-medida") String unidadeMedida ) {
        return service.findByUnidadeMedida(unidadeMedida).stream().map(i -> IngredienteMapper.toResponseDTO(i)).toList();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid IngredienteRequestDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
