package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.ModoPreparo;
import br.unitins.tp1.projeto.service.ModoPreparoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class ModoPreparoResourceHttpContractTest {

    private static final String BASE_URL = "/modo-preparo";

    @InjectMock
    ModoPreparoService modoPreparoService;

    @BeforeEach
    void setUp() {
        reset(modoPreparoService);
    }

    // -------------------------------------------------------------------------
    // GET /modo-preparo
    // -------------------------------------------------------------------------

    @Test
    void deveListarModosPreparoComStatus200() {
        when(modoPreparoService.findAll(anyInt(), anyInt())).thenReturn(List.of(
            modoPreparo(1L, "Cozinhar no vapor por 40 minutos", 40),
            modoPreparo(2L, "Assar em forno médio por 30 minutos", 30)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].descricao", equalTo("Cozinhar no vapor por 40 minutos"))
            .body("[0].tempoPreparoMinutos", equalTo(40));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(modoPreparoService.findAll(anyInt(), anyInt())).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    @Test
    void devePaginarResultados() {
        when(modoPreparoService.findAll(0, 1)).thenReturn(List.of(
            modoPreparo(1L, "Cozinhar no vapor por 40 minutos", 40)
        ));

        given()
            .accept(ContentType.JSON)
            .queryParam("page", 0)
            .queryParam("size", 1)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200);

        verify(modoPreparoService).findAll(0, 1);
    }

    // -------------------------------------------------------------------------
    // GET /modo-preparo/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(modoPreparoService.findById(1L))
            .thenReturn(modoPreparo(1L, "Cozinhar no vapor por 40 minutos", 40));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("descricao", equalTo("Cozinhar no vapor por 40 minutos"))
            .body("tempoPreparoMinutos", equalTo(40));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(modoPreparoService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // GET /modo-preparo/find/descricao/{descricao}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorDescricaoComStatus200() {
        when(modoPreparoService.findByDescricao("vapor")).thenReturn(List.of(
            modoPreparo(1L, "Cozinhar no vapor por 40 minutos", 40)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/descricao/vapor")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].tempoPreparoMinutos", equalTo(40));
    }

    @Test
    void deveBuscarPorDescricaoRetornarListaVazia() {
        when(modoPreparoService.findByDescricao("inexistente")).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/descricao/inexistente")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // GET /modo-preparo/order/tempo
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarOrdenadosPorTempoComStatus200() {
        when(modoPreparoService.findOrderByTempo()).thenReturn(List.of(
            modoPreparo(2L, "Assar em forno médio por 30 minutos", 30),
            modoPreparo(1L, "Cozinhar no vapor por 40 minutos", 40)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/order/tempo")
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].tempoPreparoMinutos", equalTo(30))
            .body("[1].tempoPreparoMinutos", equalTo(40));
    }

    // -------------------------------------------------------------------------
    // POST /modo-preparo
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarModoPreparoComStatus201() {
        when(modoPreparoService.create(any(ModoPreparo.class)))
            .thenReturn(modoPreparo(10L, "Cozinhar no vapor por 40 minutos", 40));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar no vapor por 40 minutos\",\"tempoPreparoMinutos\":40}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("descricao", equalTo("Cozinhar no vapor por 40 minutos"))
            .body("tempoPreparoMinutos", equalTo(40));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar no vapor\",\"tempoPreparoMinutos\":40}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(modoPreparoService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoDescricaoEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"\",\"tempoPreparoMinutos\":40}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(modoPreparoService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoTempoNegativo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar no vapor\",\"tempoPreparoMinutos\":-10}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(modoPreparoService, never()).create(any());
    }

    // -------------------------------------------------------------------------
    // PUT /modo-preparo/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarModoPreparoComStatus204() {
        doNothing().when(modoPreparoService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar no vapor por 35 minutos\",\"tempoPreparoMinutos\":35}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(modoPreparoService).update(any(Long.class), any());
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar no vapor\",\"tempoPreparoMinutos\":40}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(modoPreparoService, never()).update(any(), any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("descricao", "Já existe um modo de preparo com esta descrição"))
            .when(modoPreparoService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar no vapor por 40 minutos\",\"tempoPreparoMinutos\":40}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(422)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors[0].field", equalTo("descricao"))
            .body("timestamp", notNullValue());
    }

    // -------------------------------------------------------------------------
    // DELETE /modo-preparo/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRemoverModoPreparoComStatus204() {
        doNothing().when(modoPreparoService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(modoPreparoService).delete(1L);
    }

    @Test
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(modoPreparoService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private ModoPreparo modoPreparo(Long id, String descricao, int tempo) {
        ModoPreparo modo = new ModoPreparo();
        modo.setId(id);
        modo.setDescricao(descricao);
        modo.setTempoPreparoMinutos(tempo);
        return modo;
    }
}
