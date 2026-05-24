package br.unitins.tp1.projeto.resource;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.node.NullNode;

import br.unitins.tp1.projeto.model.UnidadeMedida;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/unidade-medida")
@Produces(MediaType.APPLICATION_JSON)
public class UnidadeMedidaResource {

    @GET
    @SuppressWarnings("null")
    public List<UnidadeMedida> buscarTodos() {
        return Arrays.asList(UnidadeMedida.values());
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        UnidadeMedida unidade = UnidadeMedida.valueOf(id);
        if (unidade == null) {
            return Response.ok(NullNode.getInstance()).build();
        }
        return Response.ok(unidade).build();
    }
}
