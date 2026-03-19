package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.model.SaborPamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sabor-pamonha")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SaborPamonhaResource {

    PamonhaService service;

    @GET
    public SaborPamonha[] buscarTodos() {
        return SaborPamonha.values();
    }

    @GET
    @Path("/{id}")
    public SaborPamonha encontrarPorId(Long id) {
        return SaborPamonha.valueOf(id);
    }
}
