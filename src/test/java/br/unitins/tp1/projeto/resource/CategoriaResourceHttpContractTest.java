package br.unitins.tp1.projeto.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
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
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.service.CategoriaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class CategoriaResourceHttpContractTest {

    private static final String BASE_URL = "/categorias";

    @InjectMock
    CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        reset(categoriaService);
    }

    // -------------------------------------------------------------------------
    // GET /categorias
    // -------------------------------------------------------------------------

    @Test
    void deveListarCategoriasComStatus200() {
        when(categoriaService.findAll(anyInt(), anyInt())).thenReturn(List.of(
            categoria(1L, "Doce", "Pamonhas doces"),
            categoria(2L, "Salgado", "Pamonhas salgadas")
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
            .body("[0].descricao", equalTo("Pamonhas doces"));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(categoriaService.findAll(anyInt(), anyInt())).thenReturn(List.of());

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
        when(categoriaService.findAll(1, 5)).thenReturn(List.of(
            categoria(6L, "Especial", "Pamonhas especiais")
        ));

        given()
            .accept(ContentType.JSON)
            .queryParam("page", 1)
            .queryParam("size", 5)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(1));

        verify(categoriaService).findAll(1, 5);
    }

    // -------------------------------------------------------------------------
    // GET /categorias/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(categoriaService.findById(1L)).thenReturn(categoria(1L, "Doce", "Pamonhas doces"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("nome", equalTo("Doce"))
            .body("descricao", equalTo("Pamonhas doces"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(categoriaService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // GET /categorias/find/nome/{nome}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorNomeComStatus200() {
        when(categoriaService.findByNome("Doce")).thenReturn(List.of(
            categoria(1L, "Doce", "Pamonhas doces")
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/nome/Doce")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].nome", equalTo("Doce"));
    }

    @Test
    void deveBuscarPorNomeRetornarListaVazia() {
        when(categoriaService.findByNome("Inexistente")).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/nome/Inexistente")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // GET /categorias/find/com-pamonhas
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarCategoriasComPamonhasComStatus200() {
        when(categoriaService.findComPamonhas()).thenReturn(List.of(
            categoria(2L, "Especial", "Pamonhas especiais"),
            categoria(3L, "Doces", "Pamonhas doces")
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/com-pamonhas")
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    // -------------------------------------------------------------------------
    // POST /categorias
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarCategoriaComStatus201() {
        when(categoriaService.create(any(Categoria.class)))
            .thenReturn(categoria(10L, "Doce", "Pamonhas doces"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pamonhas doces\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Doce"));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pamonhas doces\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(categoriaService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoNomeEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"descricao\":\"Pamonhas doces\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(categoriaService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoDescricaoEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(categoriaService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar400QuandoJsonForMalformado() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pamonhas\"")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(400);
    }

    // -------------------------------------------------------------------------
    // PUT /categorias/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarCategoriaComStatus204() {
        doNothing().when(categoriaService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce Atualizado\",\"descricao\":\"Pamonhas doces premium\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(categoriaService).update(any(Long.class), any());
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pamonhas doces\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(categoriaService, never()).update(any(), any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("nome", "Já existe uma categoria com este nome"))
            .when(categoriaService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Doce\",\"descricao\":\"Pamonhas doces\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(422)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("detail", containsString("Já existe uma categoria com este nome"))
            .body("instance", equalTo("/categorias/1"))
            .body("errors[0].field", equalTo("nome"))
            .body("errors[0].message", containsString("Já existe uma categoria com este nome"))
            .body("timestamp", notNullValue());
    }

    // -------------------------------------------------------------------------
    // DELETE /categorias/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
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
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(categoriaService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Content negotiation
    // -------------------------------------------------------------------------

    @Test
    void deveRetornar406QuandoAcceptNaoForJSON() {
        when(categoriaService.findAll(anyInt(), anyInt())).thenReturn(List.of());

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
            .body("nome=Doce&descricao=Pamonhas doces")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(415);
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Categoria categoria(Long id, String nome, String descricao) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNome(nome);
        c.setDescricao(descricao);
        return c;
    }
}
