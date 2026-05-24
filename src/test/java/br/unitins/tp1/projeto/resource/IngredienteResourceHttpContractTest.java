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

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.UnidadeMedida;
import br.unitins.tp1.projeto.service.IngredienteService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class IngredienteResourceHttpContractTest {

    private static final String BASE_URL = "/ingredientes";

    @InjectMock
    IngredienteService ingredienteService;

    @BeforeEach
    void setUp() {
        reset(ingredienteService);
    }

    // -------------------------------------------------------------------------
    // GET /ingredientes
    // -------------------------------------------------------------------------

    @Test
    void deveListarIngredientesComStatus200() {
        when(ingredienteService.findAll(anyInt(), anyInt())).thenReturn(List.of(
            ingrediente(1L, "Milho Verde", BigDecimal.valueOf(3.50), 100.0, UnidadeMedida.KG),
            ingrediente(2L, "Açúcar", BigDecimal.valueOf(0.01), 50000.0, UnidadeMedida.GRAMA)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Milho Verde"));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(ingredienteService.findAll(anyInt(), anyInt())).thenReturn(List.of());

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
        when(ingredienteService.findAll(0, 3)).thenReturn(List.of(
            ingrediente(1L, "Milho Verde", BigDecimal.valueOf(3.50), 100.0, UnidadeMedida.KG)
        ));

        given()
            .accept(ContentType.JSON)
            .queryParam("page", 0)
            .queryParam("size", 3)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200);

        verify(ingredienteService).findAll(0, 3);
    }

    // -------------------------------------------------------------------------
    // GET /ingredientes/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(ingredienteService.findById(1L))
            .thenReturn(ingrediente(1L, "Milho Verde", BigDecimal.valueOf(3.50), 100.0, UnidadeMedida.KG));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("nome", equalTo("Milho Verde"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(ingredienteService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // GET /ingredientes/find/estoque-abaixo/{estoque-abaixo}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorEstoqueAbaixoComStatus200() {
        when(ingredienteService.findByEstoqueAbaixo(50.0)).thenReturn(List.of(
            ingrediente(2L, "Queijo", BigDecimal.valueOf(25.00), 15.0, UnidadeMedida.KG)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/estoque-abaixo/50.0")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].nome", equalTo("Queijo"));
    }

    @Test
    void deveBuscarPorEstoqueAbaixoRetornarListaVazia() {
        when(ingredienteService.findByEstoqueAbaixo(1.0)).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/estoque-abaixo/1.0")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // GET /ingredientes/find/unidade-medida/{unidade-medida}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorUnidadeMedidaComStatus200() {
        when(ingredienteService.findByUnidadeMedida("KG")).thenReturn(List.of(
            ingrediente(1L, "Milho Verde", BigDecimal.valueOf(3.50), 100.0, UnidadeMedida.KG),
            ingrediente(5L, "Queijo", BigDecimal.valueOf(25.00), 15.0, UnidadeMedida.KG)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/unidade-medida/KG")
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    // -------------------------------------------------------------------------
    // POST /ingredientes
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarIngredienteComStatus201() {
        when(ingredienteService.create(any(Ingrediente.class)))
            .thenReturn(ingrediente(10L, "Milho Verde", BigDecimal.valueOf(3.50), 100.0, UnidadeMedida.KG));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde\",\"precoUnitario\":3.50,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Milho Verde"));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde\",\"precoUnitario\":3.50,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(ingredienteService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoNomeEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"precoUnitario\":3.50,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(ingredienteService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoPrecoNegativo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde\",\"precoUnitario\":-1.0,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(ingredienteService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoUnidadeMedidaNula() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde\",\"precoUnitario\":3.50,\"estoque\":100.0,\"idUnidadeMedida\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(ingredienteService, never()).create(any());
    }

    // -------------------------------------------------------------------------
    // PUT /ingredientes/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarIngredienteComStatus204() {
        doNothing().when(ingredienteService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde Premium\",\"precoUnitario\":4.00,\"estoque\":120.0,\"idUnidadeMedida\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(ingredienteService).update(any(Long.class), any());
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde\",\"precoUnitario\":3.50,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(ingredienteService, never()).update(any(), any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("nome", "Já existe um ingrediente com este nome"))
            .when(ingredienteService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Verde\",\"precoUnitario\":3.50,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(422)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors[0].field", equalTo("nome"))
            .body("timestamp", notNullValue());
    }

    // -------------------------------------------------------------------------
    // DELETE /ingredientes/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRemoverIngredienteComStatus204() {
        doNothing().when(ingredienteService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(ingredienteService).delete(1L);
    }

    @Test
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(ingredienteService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Ingrediente ingrediente(Long id, String nome, BigDecimal preco, double estoque, UnidadeMedida unidade) {
        Ingrediente ing = new Ingrediente();
        ing.setId(id);
        ing.setNome(nome);
        ing.setPrecoUnitario(preco);
        ing.setEstoque(estoque);
        ing.setUnidadeMedida(unidade);
        return ing;
    }
}
