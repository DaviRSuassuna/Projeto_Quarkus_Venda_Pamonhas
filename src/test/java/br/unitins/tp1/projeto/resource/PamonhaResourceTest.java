package br.unitins.tp1.projeto.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.dto.ItemReceitaRequestDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.dto.TabelaNutricionalRequestDTO;
import br.unitins.tp1.projeto.model.UnidadeMedida;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class PamonhaResourceTest {

    @InjectMock
    PamonhaService service;

    @Inject
    PamonhaResource resource;

    @Test
    public void testIncluir() {
        TabelaNutricionalRequestDTO tabela = new TabelaNutricionalRequestDTO(100.0, 20.0, 10.0, 5.0, 2.0, 1.0);
        ModoPreparoRequestDTO modo = new ModoPreparoRequestDTO("Modo de Preparo", 30);
        EmbalagemRequestDTO embalagem = new EmbalagemRequestDTO("Tipo", "Desc", java.math.BigDecimal.valueOf(10), 5.0, "Plastico", true, "Mat", 30);
        List<CategoriaRequestDTO> categorias = Arrays.asList(new CategoriaRequestDTO("Cat", "Desc"));
        List<ItemReceitaRequestDTO> itens = Arrays.asList(new ItemReceitaRequestDTO(1.0, UnidadeMedida.KG, 1L));
        PamonhaRequestDTO dto = new PamonhaRequestDTO("Pamonha Teste", "Descrição Teste", java.math.BigDecimal.valueOf(5.0), 10, tabela, modo, embalagem, categorias, itens);
        PamonhaResponseDTO responseDTO = new PamonhaResponseDTO(1L, "Pamonha Teste", null, null, null, null, null, null, null, null);

        when(service.create(dto)).thenReturn(responseDTO);

        Response response = resource.incluir(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testBuscarTodos() {
        Pamonha pamonha1 = new Pamonha();
        pamonha1.setId(1L);
        pamonha1.setNome("Pamonha 1");

        Pamonha pamonha2 = new Pamonha();
        pamonha2.setId(2L);
        pamonha2.setNome("Pamonha 2");

        when(service.findAll()).thenReturn(Arrays.asList(pamonha1, pamonha2));

        List<PamonhaResponseDTO> result = resource.buscarTodos();

        assertEquals(2, result.size());
    }

    @Test
    public void testEncontrarPorId() {
        Pamonha pamonha = new Pamonha();
        pamonha.setId(1L);
        pamonha.setNome("Pamonha Teste");

        when(service.findById(1L)).thenReturn(pamonha);

        PamonhaResponseDTO result = resource.encontrarPorId(1L);

        assertNotNull(result);
        assertEquals("Pamonha Teste", result.nome());
    }

    @Test
    public void testEncontrarPorNome() {
        Pamonha pamonha = new Pamonha();
        pamonha.setId(1L);
        pamonha.setNome("Pamonha Teste");

        when(service.findByNome("Pamonha Teste")).thenReturn(Arrays.asList(pamonha));

        List<PamonhaResponseDTO> result = resource.encontrarPorNome("Pamonha Teste");

        assertEquals(1, result.size());
    }

    @Test
    public void testEncontrarPorCategoria() {
        Pamonha pamonha = new Pamonha();
        pamonha.setId(1L);
        pamonha.setNome("Pamonha Teste");

        when(service.findByCategoria(1L)).thenReturn(Arrays.asList(pamonha));

        List<PamonhaResponseDTO> result = resource.encontrarPorCategoria(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void testAtualizar() {
        TabelaNutricionalRequestDTO tabela = new TabelaNutricionalRequestDTO(150.0, 25.0, 12.0, 6.0, 3.0, 2.0);
        ModoPreparoRequestDTO modo = new ModoPreparoRequestDTO("Modo Atualizado", 45);
        EmbalagemRequestDTO embalagem = new EmbalagemRequestDTO("Tipo Atualizado", "Desc Atualizada", java.math.BigDecimal.valueOf(15), 10.0, "Plastico Atualizado", false, "Mat Atualizado", 60);
        List<CategoriaRequestDTO> categorias = Arrays.asList(new CategoriaRequestDTO("Cat Atualizada", "Desc Cat"));
        List<ItemReceitaRequestDTO> itens = Arrays.asList(new ItemReceitaRequestDTO(2.0, UnidadeMedida.KG, 2L));
        PamonhaRequestDTO dto = new PamonhaRequestDTO("Pamonha Atualizada", "Descrição Atualizada", java.math.BigDecimal.valueOf(7.0), 20, tabela, modo, embalagem, categorias, itens);

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