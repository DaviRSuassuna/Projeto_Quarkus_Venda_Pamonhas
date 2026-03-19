package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.mapper.PamonhaMapper;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
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

@Path("/pamonhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PamanhoResource {

    @Inject

    PamonhaService service;

    @POST
    @Transactional
    public PamonhaResponseDTO incluir(PamonhaRequestDTO dto) {
        Pamonha pamonha = service.create(PamonhaMapper.toEntity(dto));
        return PamonhaMapper.toResponseDTO(pamonha);
    }

    @GET
    public List<PamonhaResponseDTO> buscarTodos() {
        return service.findAll().stream().map(p -> PamonhaMapper.toResponseDTO(p)).toList();
    }

    @GET
    @Path("/{id}")
    public PamonhaResponseDTO encontrarPorId(@PathParam("id") long id) {
        return PamonhaMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/{ingrediente_principal}")
    public List<PamonhaResponseDTO> encontrarPorIngredientePrincipal(@PathParam("ingrediente_principal") String ingredientePrincipal) {
        return service.findByIngredientePrincipal(ingredientePrincipal).stream().map(p -> PamonhaMapper.toResponseDTO(p)).toList();
    }

    @PUT
    @Path("/{id}")
    public void atualizar(@PathParam("id") long id, PamonhaRequestDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        service.delete(id);
    }
}
