package br.unitins.tp1.projeto.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.dto.EmbalagemResponseDTO;
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import br.unitins.tp1.projeto.service.EmbalagemService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class EmbalagemResourceTest {

    @InjectMock
    EmbalagemService service;

    @Inject
    EmbalagemResource resource;

    @Test
    public void testIncluirPlastica() {
        EmbalagemRequestDTO dto = new EmbalagemRequestDTO(
                "PLASTICA",
                "Descrição Teste Plástica",
                BigDecimal.valueOf(10.0),
                5.0,
                "PVC",
                true,
                null,  // material não aplicável para plástica
                null   // tempoDecomposicaoDias não aplicável para plástica
        );

        EmbalagemPlastica embalagem = new EmbalagemPlastica();
        embalagem.setId(1L);
        embalagem.setDescricao("Descrição Teste Plástica");
        embalagem.setCusto(BigDecimal.valueOf(10.0));
        embalagem.setPesoSuportado(5.0);
        embalagem.setTipoPlastico("PVC");
        embalagem.setReciclavel(true);

        when(service.create(dto)).thenReturn(embalagem);

        Response response = resource.incluir(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testIncluirBiodegradavel() {
        EmbalagemRequestDTO dto = new EmbalagemRequestDTO(
                "BIODEGRADAVEL",
                "Descrição Teste Biodegradável",
                BigDecimal.valueOf(15.0),
                3.0,
                null,  // tipoPlastico não aplicável para biodegradável
                null,  // reciclavel não aplicável para biodegradável
                "Papel",
                20
        );

        EmbalagemBiodegradavel embalagem = new EmbalagemBiodegradavel();
        embalagem.setId(2L);
        embalagem.setDescricao("Descrição Teste Biodegradável");
        embalagem.setCusto(BigDecimal.valueOf(15.0));
        embalagem.setPesoSuportado(3.0);
        embalagem.setMaterial("Papel");
        embalagem.setTempoDecomposicaoDias(20);

        when(service.create(dto)).thenReturn(embalagem);

        Response response = resource.incluir(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testBuscarTodos() {
        EmbalagemPlastica embalagem1 = new EmbalagemPlastica();
        embalagem1.setId(1L);
        embalagem1.setDescricao("Plástica 1");
        embalagem1.setCusto(BigDecimal.valueOf(10.0));
        embalagem1.setPesoSuportado(5.0);
        embalagem1.setTipoPlastico("PVC");
        embalagem1.setReciclavel(true);

        EmbalagemBiodegradavel embalagem2 = new EmbalagemBiodegradavel();
        embalagem2.setId(2L);
        embalagem2.setDescricao("Biodegradável 1");
        embalagem2.setCusto(BigDecimal.valueOf(15.0));
        embalagem2.setPesoSuportado(3.0);
        embalagem2.setMaterial("Papel");
        embalagem2.setTempoDecomposicaoDias(20);

        when(service.findAll()).thenReturn(Arrays.asList(embalagem1, embalagem2));

        List<EmbalagemResponseDTO> result = resource.buscarTodos();

        assertEquals(2, result.size());
    }

    @Test
    public void testEncontrarPorId() {
        EmbalagemPlastica embalagem = new EmbalagemPlastica();
        embalagem.setId(1L);
        embalagem.setDescricao("Plástica Teste");
        embalagem.setCusto(BigDecimal.valueOf(10.0));
        embalagem.setPesoSuportado(5.0);
        embalagem.setTipoPlastico("PVC");
        embalagem.setReciclavel(true);

        when(service.findById(1L)).thenReturn(embalagem);

        EmbalagemResponseDTO result = resource.encontrarPorId(1L);

        assertNotNull(result);
        assertEquals("PLASTICA", result.tipo());
        assertEquals("Plástica Teste", result.descricao());
    }

    @Test
    public void testEncontrarPorTipo() {
        EmbalagemPlastica embalagem = new EmbalagemPlastica();
        embalagem.setId(1L);
        embalagem.setDescricao("Plástica Tipo A");
        embalagem.setCusto(BigDecimal.valueOf(10.0));
        embalagem.setPesoSuportado(5.0);
        embalagem.setTipoPlastico("PVC");
        embalagem.setReciclavel(true);

        when(service.findByTipo("PLASTICA")).thenReturn(Arrays.asList(embalagem));

        List<EmbalagemResponseDTO> result = resource.encontrarPorTipo("PLASTICA");

        assertEquals(1, result.size());
        assertEquals("PLASTICA", result.get(0).tipo());
    }

    @Test
    public void testEncontrarOrdenadasPorCusto() {
        EmbalagemBiodegradavel embalagem = new EmbalagemBiodegradavel();
        embalagem.setId(1L);
        embalagem.setDescricao("Biodegradável Barata");
        embalagem.setCusto(BigDecimal.valueOf(5.0));
        embalagem.setPesoSuportado(2.0);
        embalagem.setMaterial("Papel");
        embalagem.setTempoDecomposicaoDias(30);

        when(service.findOrderByCusto()).thenReturn(Arrays.asList(embalagem));

        List<EmbalagemResponseDTO> result = resource.encontrarOrdenadasPorCusto();

        assertEquals(1, result.size());
        assertEquals("BIODEGRADAVEL", result.get(0).tipo());
    }

    @Test
    public void testEncontrarBiodegradavelRapida() {
        EmbalagemBiodegradavel embalagem = new EmbalagemBiodegradavel();
        embalagem.setId(1L);
        embalagem.setDescricao("Biodegradável Rápida");
        embalagem.setCusto(BigDecimal.valueOf(12.0));
        embalagem.setPesoSuportado(4.0);
        embalagem.setMaterial("Composto Orgânico");
        embalagem.setTempoDecomposicaoDias(15);  // Menos de 30 dias

        when(service.findBiodegradavelRapida(30)).thenReturn(Arrays.asList(embalagem));

        List<EmbalagemResponseDTO> result = resource.encontrarBiodegradavelRapida(30);

        assertEquals(1, result.size());
        assertEquals("BIODEGRADAVEL", result.get(0).tipo());
        assertEquals(15, result.get(0).tempoDecomposicaoDias());
    }

    @Test
    public void testAtualizar() {
        EmbalagemRequestDTO dto = new EmbalagemRequestDTO(
                "PLASTICA",
                "Descrição Atualizada",
                BigDecimal.valueOf(15.0),
                10.0,
                "PET",
                false,
                null,
                null
        );

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