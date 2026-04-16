package br.unitins.tp1.projeto.resource;

import java.util.Arrays;
import java.util.List;

import br.unitins.tp1.projeto.model.UnidadeMedida;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/unidade-medida")
@Produces(MediaType.APPLICATION_JSON)
public class UnidadeMedidaResource {

    @GET
    public List<UnidadeMedida> buscarTodos() {
        return Arrays.asList(UnidadeMedida.values());
    }

    @GET
    @Path("/{id}")
    public UnidadeMedida buscarPorId(@PathParam("id") Long id) {
        return UnidadeMedida.valueOf(id);
    }
}
