package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class UnidadeMedidaResourceTest {

    private static final String BASE_URL = "/unidade-medida";

    @Test
    void deveListarTodasUnidadesComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(5));
    }

    @Test
    void deveConterTodosOsValoresEsperados() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("findAll { it.ID == 1 }.NOME[0]", equalTo("Quilograma"))
            .body("findAll { it.ID == 2 }.NOME[0]", equalTo("Grama"))
            .body("findAll { it.ID == 3 }.NOME[0]", equalTo("Litro"))
            .body("findAll { it.ID == 4 }.NOME[0]", equalTo("Mililitro"))
            .body("findAll { it.ID == 5 }.NOME[0]", equalTo("Unidade"));
    }

    @Test
    void deveBuscarKgPorIdComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("ID", equalTo(1))
            .body("NOME", equalTo("Quilograma"));
    }

    @Test
    void deveBuscarGramaPorIdComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/2")
        .then()
            .statusCode(200)
            .body("ID", equalTo(2))
            .body("NOME", equalTo("Grama"));
    }

    @Test
    void deveBuscarLitroPorIdComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/3")
        .then()
            .statusCode(200)
            .body("ID", equalTo(3))
            .body("NOME", equalTo("Litro"));
    }

    @Test
    void deveBuscarMililitroPorIdComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/4")
        .then()
            .statusCode(200)
            .body("ID", equalTo(4))
            .body("NOME", equalTo("Mililitro"));
    }

    @Test
    void deveBuscarUnidadePorIdComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/5")
        .then()
            .statusCode(200)
            .body("ID", equalTo(5))
            .body("NOME", equalTo("Unidade"));
    }

    @Test
    void deveRetornarNuloParaIdInexistente() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/99")
        .then()
            .statusCode(200)
            .body(equalTo("null"));
    }

    @Test
    void deveRetornar406QuandoAcceptNaoForJSON() {
        given()
            .accept(ContentType.XML)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(406);
    }
}
