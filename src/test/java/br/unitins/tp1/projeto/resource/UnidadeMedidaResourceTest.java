package br.unitins.tp1.projeto.resource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.model.UnidadeMedida;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UnidadeMedidaResourceTest {

    @Test
    public void testBuscarTodos() {
        UnidadeMedidaResource resource = new UnidadeMedidaResource();
        List<UnidadeMedida> result = resource.buscarTodos();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testBuscarPorId() {
        UnidadeMedidaResource resource = new UnidadeMedidaResource();
        // Assumindo que UnidadeMedida tem valores, pegar o primeiro
        UnidadeMedida expected = UnidadeMedida.values()[0];
        UnidadeMedida result = resource.buscarPorId(expected.getID());

        assertEquals(expected, result);
    }
}