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
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class IngredienteResourceHttpContractTest {

    private static final String BASE_URL = "/ingredientes";

    @InjectMock
    IngredienteService ingredienteService;

    @BeforeEach
    void setUp() {
        reset(ingredienteService);
    }

    @Test
    void deveListarIngredientesComStatus200() {
        when(ingredienteService.findAll()).thenReturn(List.of(
            ingrediente(1L, "Milho", BigDecimal.valueOf(10.0), 100.0, UnidadeMedida.KG),
            ingrediente(2L, "Sal", BigDecimal.valueOf(5.0), 50.0, UnidadeMedida.GRAMA)
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
            .body("[0].nome", equalTo("Milho"));
    }

    @Test
    void deveBuscarPorIdComStatus200() {
        when(ingredienteService.findById(1L))
            .thenReturn(ingrediente(1L, "Milho", BigDecimal.valueOf(10.0), 100.0, UnidadeMedida.KG));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Milho"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(ingredienteService.findById(999L)).thenThrow(new NotFoundException("Ingrediente não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarIngredienteComStatus201() {
        when(ingredienteService.create(any(Ingrediente.class)))
            .thenReturn(ingrediente(10L, "Milho", BigDecimal.valueOf(10.0), 100.0, UnidadeMedida.KG));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho\",\"precoUnitario\":10.0,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Milho"));
    }

    @Test
    void deveAtualizarIngredienteComStatus204() {
        doNothing().when(ingredienteService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho Premium\",\"precoUnitario\":15.0,\"estoque\":150.0,\"idUnidadeMedida\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(ingredienteService).update(any(Long.class), any());
    }

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
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"precoUnitario\":-1.0,\"estoque\":-10.0,\"idUnidadeMedida\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(ingredienteService, never()).create(any(Ingrediente.class));
    }

    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("nome", "Já existe um ingrediente com este nome"))
            .when(ingredienteService)
            .update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Milho\",\"precoUnitario\":10.0,\"estoque\":100.0,\"idUnidadeMedida\":1}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors[0].field", equalTo("nome"))
            .body("timestamp", notNullValue());
    }

    private Ingrediente ingrediente(Long id, String nome, BigDecimal precoUnitario, 
                                    double estoque, UnidadeMedida unidade) {
        Ingrediente ing = new Ingrediente();
        ing.setId(id);
        ing.setNome(nome);
        ing.setPrecoUnitario(precoUnitario);
        ing.setEstoque(estoque);
        ing.setUnidadeMedida(unidade);
        return ing;
    }
}
