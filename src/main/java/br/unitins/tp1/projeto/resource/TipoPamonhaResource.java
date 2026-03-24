package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.model.TipoPamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/tipo-pamonhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoPamonhaResource {

    PamonhaService service;

    @GET
    public TipoPamonha[] buscarTodos() {
        return TipoPamonha.values();
    }

    @GET
    @Path("/{id}")
    public TipoPamonha encontrarPorId(Long id) {
        return TipoPamonha.valueOf(id);
    }
}
