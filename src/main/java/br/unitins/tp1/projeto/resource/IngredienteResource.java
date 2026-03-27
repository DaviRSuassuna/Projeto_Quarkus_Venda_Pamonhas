package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.dto.IngredienteResponseDTO;
import br.unitins.tp1.projeto.mapper.IngredienteMapper;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.service.IngredienteService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ingredientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IngredienteResource {

    @Inject

    IngredienteService service;

    @POST
    @Transactional
    public IngredienteResponseDTO incluir(IngredienteRequestDTO dto) {
        Ingrediente ingrediente = service.create(IngredienteMapper.toEntity(dto));
        return IngredienteMapper.toResponseDTO(ingrediente);
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
    public void atualizar(@PathParam("id") Long id, IngredienteRequestDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }
}
