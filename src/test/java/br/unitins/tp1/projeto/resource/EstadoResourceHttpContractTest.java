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

import br.unitins.tp1.projeto.dto.EstadoRequestDTO;
import br.unitins.tp1.projeto.model.Estado;
import br.unitins.tp1.projeto.service.EstadoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class EstadoResourceHttpContractTest {

    private static final String BASE_URL = "/estados";

    @InjectMock
    EstadoService estadoService;

    @BeforeEach
    void setUp() {
        reset(estadoService);
    }

    // -------------------------------------------------------------------------
    // GET /estados
    // -------------------------------------------------------------------------

    @Test
    void deveListarEstadosComStatus200() {
        when(estadoService.findAll()).thenReturn(List.of(
            estado(1L, "Tocantins", "TO"),
            estado(2L, "Goiás", "GO")
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
            .body("[0].nome", equalTo("Tocantins"))
            .body("[0].sigla", equalTo("TO"));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(estadoService.findAll()).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // GET /estados/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(estadoService.findById(1L)).thenReturn(estado(1L, "Tocantins", "TO"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("nome", equalTo("Tocantins"))
            .body("sigla", equalTo("TO"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(estadoService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // POST /estados
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarEstadoComStatus201() {
        when(estadoService.create(any(EstadoRequestDTO.class)))
            .thenReturn(estado(10L, "Tocantins", "TO"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Tocantins\",\"sigla\":\"TO\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Tocantins"))
            .body("sigla", equalTo("TO"));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Tocantins\",\"sigla\":\"TO\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(estadoService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoNomeEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"sigla\":\"TO\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(estadoService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoSiglaEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Tocantins\",\"sigla\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(estadoService, never()).create(any());
    }

    // -------------------------------------------------------------------------
    // PUT /estados/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarEstadoComStatus204() {
        when(estadoService.update(any(Long.class), any(EstadoRequestDTO.class)))
            .thenReturn(estado(1L, "Tocantins Atualizado", "TO"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Tocantins Atualizado\",\"sigla\":\"TO\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(estadoService).update(any(Long.class), any(EstadoRequestDTO.class));
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Tocantins\",\"sigla\":\"TO\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(estadoService, never()).update(any(), any());
    }

    // -------------------------------------------------------------------------
    // DELETE /estados/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRemoverEstadoComStatus204() {
        doNothing().when(estadoService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(estadoService).delete(1L);
    }

    @Test
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(estadoService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Content negotiation
    // -------------------------------------------------------------------------

    @Test
    void deveRetornar406QuandoAcceptNaoForJSON() {
        when(estadoService.findAll()).thenReturn(List.of());

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
            .body("nome=Tocantins&sigla=TO")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(415);
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Estado estado(Long id, String nome, String sigla) {
        Estado e = new Estado();
        e.setId(id);
        e.setNome(nome);
        e.setSigla(sigla);
        return e;
    }
}
