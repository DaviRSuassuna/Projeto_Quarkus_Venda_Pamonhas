package br.unitins.tp1.projeto.resource;

import java.util.Arrays;
import java.util.List;

import br.unitins.tp1.projeto.model.StatusPedido;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/status-pedido")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Status de Pedido", description = "Consulta dos status de pedido disponíveis no sistema")
public class StatusPedidoResource {

    @GET
    @Operation(summary = "Listar status de pedido", description = "Retorna todos os status de pedido disponíveis no sistema")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<StatusPedido> buscarTodos() {
        return Arrays.asList(StatusPedido.values());
    }
}
