package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
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
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class ModoPreparoResourceHttpContractTest {

    private static final String BASE_URL = "/modo-preparo";

    @InjectMock
    ModoPreparoService modoPreparoService;

    @BeforeEach
    void setUp() {
        reset(modoPreparoService);
    }

    @Test
    void deveListarModosPreparoComStatus200() {
        when(modoPreparoService.findAll()).thenReturn(List.of(
            modoPreparo(1L, "Cozinhar em vapor", 30),
            modoPreparo(2L, "Assar no forno", 45)
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
            .body("[0].descricao", equalTo("Cozinhar em vapor"));
    }

    @Test
    void deveBuscarPorIdComStatus200() {
        when(modoPreparoService.findById(1L))
            .thenReturn(modoPreparo(1L, "Cozinhar em vapor", 30));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("descricao", equalTo("Cozinhar em vapor"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(modoPreparoService.findById(999L))
            .thenThrow(new NotFoundException("ModoPreparo não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarModoPreparoComStatus201() {
        when(modoPreparoService.create(any(ModoPreparo.class)))
            .thenReturn(modoPreparo(10L, "Cozinhar em vapor", 30));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar em vapor\",\"tempoPreparoMinutos\":30}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("descricao", equalTo("Cozinhar em vapor"));
    }

    @Test
    void deveAtualizarModoPreparoComStatus204() {
        doNothing().when(modoPreparoService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar em vapor rápido\",\"tempoPreparoMinutos\":25}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(modoPreparoService).update(any(Long.class), any());
    }

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
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"\",\"tempoPreparoMinutos\":-1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(modoPreparoService, never()).create(any(ModoPreparo.class));
    }

    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("descricao", "Já existe um modo de preparo com esta descrição"))
            .when(modoPreparoService)
            .update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"descricao\":\"Cozinhar em vapor\",\"tempoPreparoMinutos\":30}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors[0].field", equalTo("descricao"))
            .body("timestamp", notNullValue());
    }

    private ModoPreparo modoPreparo(Long id, String descricao, int tempoPreparoMinutos) {
        ModoPreparo modo = new ModoPreparo();
        modo.setId(id);
        modo.setDescricao(descricao);
        modo.setTempoPreparoMinutos(tempoPreparoMinutos);
        return modo;
    }
}
