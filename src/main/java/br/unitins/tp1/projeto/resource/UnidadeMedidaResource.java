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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/unidade-medida")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Unidades de Medida", description = "Consulta das unidades de medida disponíveis no sistema")
public class UnidadeMedidaResource {

    @GET
    @SuppressWarnings("null")
    @Operation(summary = "Listar unidades de medida", description = "Retorna todas as unidades de medida disponíveis no sistema")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    public List<UnidadeMedida> buscarTodos() {
        return Arrays.asList(UnidadeMedida.values());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar unidade de medida por ID", description = "Retorna a unidade de medida correspondente ao ID informado")
    @APIResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Unidade de medida não encontrada")
    public Response buscarPorId(@PathParam("id") Long id) {
        UnidadeMedida unidade = UnidadeMedida.valueOf(id);
        if (unidade == null) {
            return Response.ok(NullNode.getInstance()).build();
        }
        return Response.ok(unidade).build();
    }
}
