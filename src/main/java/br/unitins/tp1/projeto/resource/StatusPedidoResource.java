package br.unitins.tp1.projeto.resource;

import br.unitins.tp1.projeto.model.StatusPedido;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/status-pedido")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatusPedidoResource {

    @GET
    public StatusPedido[] buscarTodos() {
        return StatusPedido.values();
    }

    @GET
    @Path("/{id}")
    public StatusPedido encontrarPorId(@PathParam("id") Long id) {
        return StatusPedido.valueOf(id);
    }
}