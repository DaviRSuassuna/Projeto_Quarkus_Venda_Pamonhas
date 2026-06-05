package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.dto.CidadeRequestDTO;
import br.unitins.tp1.projeto.model.Cidade;
import br.unitins.tp1.projeto.model.Estado;
import br.unitins.tp1.projeto.service.CidadeService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class CidadeResourceHttpContractTest {

    private static final String BASE_URL = "/cidades";

    @InjectMock
    CidadeService cidadeService;

    @BeforeEach
    void setUp() {
        reset(cidadeService);
    }

    // -------------------------------------------------------------------------
    // GET /cidades
    // -------------------------------------------------------------------------

    @Test
    void deveListarCidadesComStatus200() {
        when(cidadeService.findAll()).thenReturn(List.of(
            cidade(1L, "Palmas", "Tocantins", "TO"),
            cidade(2L, "Araguaína", "Tocantins", "TO")
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Palmas"))
            .body("[0].estado", equalTo("Tocantins"));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(cidadeService.findAll()).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // GET /cidades/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(cidadeService.findById(1L)).thenReturn(cidade(1L, "Palmas", "Tocantins", "TO"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("nome", equalTo("Palmas"))
            .body("estado", equalTo("Tocantins"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(cidadeService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // GET /cidades/estado/{estadoId}
    // -------------------------------------------------------------------------

    @Test
    void deveListarCidadesPorEstadoComStatus200() {
        when(cidadeService.findByEstado(1L)).thenReturn(List.of(
            cidade(1L, "Palmas", "Tocantins", "TO"),
            cidade(2L, "Araguaína", "Tocantins", "TO")
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/estado/1")
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].estado", equalTo("Tocantins"));
    }

    // -------------------------------------------------------------------------
    // POST /cidades
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarCidadeComStatus201() {
        when(cidadeService.create(any(CidadeRequestDTO.class)))
            .thenReturn(cidade(10L, "Palmas", "Tocantins", "TO"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Palmas\",\"estadoId\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Palmas"))
            .body("estado", equalTo("Tocantins"));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Palmas\",\"estadoId\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(cidadeService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoNomeEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"estadoId\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(cidadeService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoEstadoIdNulo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Palmas\",\"estadoId\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(cidadeService, never()).create(any());
    }

    // -------------------------------------------------------------------------
    // PUT /cidades/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarCidadeComStatus204() {
        when(cidadeService.update(any(Long.class), any(CidadeRequestDTO.class)))
            .thenReturn(cidade(1L, "Palmas Atualizada", "Tocantins", "TO"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Palmas Atualizada\",\"estadoId\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(cidadeService).update(any(Long.class), any(CidadeRequestDTO.class));
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Palmas\",\"estadoId\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(cidadeService, never()).update(any(), any());
    }

    // -------------------------------------------------------------------------
    // DELETE /cidades/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRemoverCidadeComStatus204() {
        doNothing().when(cidadeService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(cidadeService).delete(1L);
    }

    @Test
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(cidadeService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Content negotiation
    // -------------------------------------------------------------------------

    @Test
    void deveRetornar406QuandoAcceptNaoForJSON() {
        when(cidadeService.findAll()).thenReturn(List.of());

        given()
            .accept(ContentType.XML)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(406);
    }

    @Test
    void deveRetornar415QuandoContentTypeNaoForJSON() {
        given()
            .contentType(ContentType.TEXT)
            .accept(ContentType.JSON)
            .body("nome=Palmas&estadoId=1")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(415);
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Cidade cidade(Long id, String nome, String estadoNome, String estadoSigla) {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNome(estadoNome);
        estado.setSigla(estadoSigla);

        Cidade c = new Cidade();
        c.setId(id);
        c.setNome(nome);
        c.setEstado(estado);
        return c;
    }
}
