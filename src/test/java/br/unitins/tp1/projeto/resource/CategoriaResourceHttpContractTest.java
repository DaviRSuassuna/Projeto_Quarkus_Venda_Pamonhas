package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
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
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.service.CategoriaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class CategoriaResourceHttpContractTest {

    private static final String BASE_URL = "/categorias";

    @InjectMock
    CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        reset(categoriaService);
    }

    @Test
    void deveListarCategoriasComStatus200() {
        when(categoriaService.findAll()).thenReturn(List.of(
            categoria(1L, "Doce", "Pães doces"),
            categoria(2L, "Salgado", "Pães salgados")
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
            .body("[0].nome", equalTo("Doce"))
            .body("[0].descricao", equalTo("Pães doces"));
    }

    @Test
    void deveBuscarPorIdComStatus200() {
        when(categoriaService.findById(1L)).thenReturn(categoria(1L, "Doce", "Pães doces"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Doce"))
            .body("descricao", equalTo("Pães doces"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(categoriaService.findById(999L)).thenThrow(new NotFoundException("Categoria não encontrada"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarCategoriaComStatus201() {
        when(categoriaService.create(any(Categoria.class)))
            .thenReturn(categoria(10L, "Doce", "Pães doces"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pães doces\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Doce"))
            .body("descricao", equalTo("Pães doces"));
    }

    @Test
    void deveAtualizarCategoriaComStatus204() {
        doNothing().when(categoriaService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce Atualizado\",\"descricao\":\"Pães doces premium\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(categoriaService).update(any(Long.class), any());
    }

    @Test
    void deveRemoverCategoriaComStatus204() {
        doNothing().when(categoriaService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(categoriaService).delete(1L);
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"descricao\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("detail", equalTo("Um ou mais campos não passaram na validação."))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(categoriaService, never()).create(any(Categoria.class));
    }

    @Test
    void deveRetornar400QuandoJsonForMalformado() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pães\"")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(400);
    }

    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("nome", "Já existe uma categoria com este nome"))
            .when(categoriaService)
            .update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pães doces\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("detail", containsString("Já existe uma categoria com este nome"))
            .body("instance", equalTo("/categorias/1"))
            .body("errors[0].field", equalTo("nome"))
            .body("errors[0].message", containsString("Já existe uma categoria com este nome"))
            .body("timestamp", notNullValue());
    }

    @Test
    void deveRespeitarHeadersAcceptEContentType() {
        when(categoriaService.findAll()).thenReturn(List.of());

        given()
            .accept(ContentType.XML)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(406);

        given()
            .contentType(ContentType.TEXT)
            .accept(ContentType.JSON)
            .body("nome=Doce&descricao=Pães doces")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(415);
    }

    private Categoria categoria(Long id, String nome, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        return categoria;
    }
}
