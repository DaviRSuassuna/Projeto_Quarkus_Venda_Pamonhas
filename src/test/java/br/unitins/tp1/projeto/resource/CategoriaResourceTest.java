package br.unitins.tp1.projeto.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.dto.CategoriaResponseDTO;
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.service.CategoriaService;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class CategoriaResourceTest {

    @InjectMock
    CategoriaService service;

    @Inject
    CategoriaResource resource  ;

    @Test
    public void testIncluir() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO("Categoria Teste", "Descrição Teste");
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");

        when(service.create(any(Categoria.class))).thenReturn(categoria);

        Response response = resource.incluir(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testBuscarTodos() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNome("Categoria 1");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNome("Categoria 2");

        when(service.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        List<CategoriaResponseDTO> result = resource.buscarTodos();

        assertEquals(2, result.size());
    }

    @Test
    public void testEncontrarPorId() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");

        when(service.findById(1L)).thenReturn(categoria);

        CategoriaResponseDTO result = resource.encontrarPorId(1L);

        assertNotNull(result);
        assertEquals("Categoria Teste", result.nome());
    }

    @Test
    public void testEncontrarPorNome() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");

        when(service.findByNome("Categoria Teste")).thenReturn(Arrays.asList(categoria));

        List<CategoriaResponseDTO> result = resource.encontrarPorNome("Categoria Teste");

        assertEquals(1, result.size());
    }

    @Test
    public void testEncontrarComPamonhas() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");

        when(service.findComPamonhas()).thenReturn(Arrays.asList(categoria));

        List<CategoriaResponseDTO> result = resource.encontrarComPamonhas();

        assertEquals(1, result.size());
    }

    @Test
    public void testAtualizar() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO("Categoria Atualizada", "Descrição Atualizada");

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