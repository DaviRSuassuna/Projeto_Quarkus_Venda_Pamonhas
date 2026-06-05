package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class StatusPedidoResourceHttpContractTest {

    private static final String BASE_URL = "/status-pedido";

    // -------------------------------------------------------------------------
    // GET /status-pedido
    // -------------------------------------------------------------------------

    @Test
    void deveListarTodosOsStatusComStatus200() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(6));
    }

    @Test
    void deveConterTodosOsValoresDoEnum() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("$", hasItem("AGUARDANDO_PAGAMENTO"))
            .body("$", hasItem("PAGO"))
            .body("$", hasItem("EM_PREPARACAO"))
            .body("$", hasItem("ENVIADO"))
            .body("$", hasItem("ENTREGUE"))
            .body("$", hasItem("CANCELADO"));
    }
}
