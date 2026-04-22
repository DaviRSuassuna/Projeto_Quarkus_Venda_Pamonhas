package br.unitins.tp1.projeto.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.dto.IngredienteRequestDTO;
import br.unitins.tp1.projeto.dto.IngredienteResponseDTO;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.service.IngredienteService;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class IngredienteResourceTest {

    @InjectMock
    IngredienteService service;

    @Inject
    IngredienteResource resource;

    @Test
    public void testIncluir() {
        IngredienteRequestDTO dto = new IngredienteRequestDTO("Ingrediente Teste", java.math.BigDecimal.valueOf(100.0), 50.0, 1L);
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNome("Ingrediente Teste");

        when(service.create(any(Ingrediente.class))).thenReturn(ingrediente);

        Response response = resource.incluir(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testBuscarTodos() {
        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setId(1L);
        ingrediente1.setNome("Ingrediente 1");

        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setId(2L);
        ingrediente2.setNome("Ingrediente 2");

        when(service.findAll()).thenReturn(Arrays.asList(ingrediente1, ingrediente2));

        List<IngredienteResponseDTO> result = resource.buscarTodos();

        assertEquals(2, result.size());
    }

    @Test
    public void testEncontrarPorId() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNome("Ingrediente Teste");

        when(service.findById(1L)).thenReturn(ingrediente);

        IngredienteResponseDTO result = resource.encontrarPorId(1L);

        assertNotNull(result);
        assertEquals("Ingrediente Teste", result.nome());
    }

    @Test
    public void testEncontrarPorEstoqueAbaixo() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNome("Ingrediente Teste");

        when(service.findByEstoqueAbaixo(50.0)).thenReturn(Arrays.asList(ingrediente));

        List<IngredienteResponseDTO> result = resource.encontrarPorEstoqueAbaixo(50.0);

        assertEquals(1, result.size());
    }

    @Test
    public void testEncontrarPorUnidadeMedida() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNome("Ingrediente Teste");

        when(service.findByUnidadeMedida("kg")).thenReturn(Arrays.asList(ingrediente));

        List<IngredienteResponseDTO> result = resource.encontrarPorUnidadeMedida("kg");

        assertEquals(1, result.size());
    }

    @Test
    public void testAtualizar() {
        IngredienteRequestDTO dto = new IngredienteRequestDTO("Ingrediente Atualizado", java.math.BigDecimal.valueOf(200.0), 100.0, 2L);

        doNothing().when(service).update(1L, dto);

        Response response = resource.atualizar(1L, dto);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDelete() {
        doNothing().when(service).delete(1L);

        Response response = resource.delete(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}