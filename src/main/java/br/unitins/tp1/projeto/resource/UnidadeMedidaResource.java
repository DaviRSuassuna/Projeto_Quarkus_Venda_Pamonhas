package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.model.UnidadeMedida;
import br.unitins.tp1.projeto.service.IngredienteService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/unidade-medida")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UnidadeMedidaResource {

    IngredienteService service;

    @GET
    public UnidadeMedida[] buscarTodos() {
        return UnidadeMedida.values();
    }

    @GET
    @Path("/{id}")
    public UnidadeMedida encontrarPorId(Long id) {
        return UnidadeMedida.valueOf(id);
    }
}