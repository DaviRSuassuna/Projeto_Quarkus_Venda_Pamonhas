package br.unitins.tp1.projeto.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoResponseDTO;
import br.unitins.tp1.projeto.model.ModoPreparo;
import br.unitins.tp1.projeto.service.ModoPreparoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class ModoPreparoResourceTest {

    @InjectMock
    ModoPreparoService service;

    @Inject
    ModoPreparoResource resource;

    @Test
    public void testIncluir() {
        ModoPreparoRequestDTO dto = new ModoPreparoRequestDTO("Descrição Teste", 30);
        ModoPreparo modo = new ModoPreparo();
        modo.setId(1L);
        modo.setDescricao("Descrição Teste");

        when(service.create(any(ModoPreparo.class))).thenReturn(modo);

        Response response = resource.incluir(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testBuscarTodos() {
        ModoPreparo modo1 = new ModoPreparo();
        modo1.setId(1L);
        modo1.setDescricao("Modo 1");

        ModoPreparo modo2 = new ModoPreparo();
        modo2.setId(2L);
        modo2.setDescricao("Modo 2");

        when(service.findAll()).thenReturn(Arrays.asList(modo1, modo2));

        List<ModoPreparoResponseDTO> result = resource.buscarTodos();

        assertEquals(2, result.size());
    }

    @Test
    public void testEncontrarPorId() {
        ModoPreparo modo = new ModoPreparo();
        modo.setId(1L);
        modo.setDescricao("Descrição Teste");

        when(service.findById(1L)).thenReturn(modo);

        ModoPreparoResponseDTO result = resource.encontrarPorId(1L);

        assertNotNull(result);
        assertEquals("Descrição Teste", result.descricao());
    }

    @Test
    public void testEncontrarPorDescricao() {
        ModoPreparo modo = new ModoPreparo();
        modo.setId(1L);
        modo.setDescricao("Descrição Teste");

        when(service.findByDescricao("Descrição Teste")).thenReturn(Arrays.asList(modo));

        List<ModoPreparoResponseDTO> result = resource.encontrarPorDescricao("Descrição Teste");

        assertEquals(1, result.size());
    }

    @Test
    public void testEncontrarOrdenadosPorTempo() {
        ModoPreparo modo = new ModoPreparo();
        modo.setId(1L);
        modo.setDescricao("Descrição Teste");

        when(service.findOrderByTempo()).thenReturn(Arrays.asList(modo));

        List<ModoPreparoResponseDTO> result = resource.encontrarOrdenadosPorTempo();

        assertEquals(1, result.size());
    }

    @Test
    public void testAtualizar() {
        ModoPreparoRequestDTO dto = new ModoPreparoRequestDTO("Descrição Atualizada", 45);

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