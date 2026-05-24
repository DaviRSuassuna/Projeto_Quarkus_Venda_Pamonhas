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

import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class PamonhaResourceHttpContractTest {

    private static final String BASE_URL = "/pamonhas";

    private static final String PAYLOAD_VALIDO = """
        {
            "nome": "Pamonha Doce Tradicional",
            "descricao": "Pamonha doce com massa de milho",
            "preco": 8.50,
            "estoque": 50,
            "tabelaNutricional": {
                "valorEnergetico": 360, "carboidratos": 80,
                "proteinas": 9, "gordurasTotais": 4, "fibras": 7, "sodio": 2
            },
            "modoPreparo": {"descricao": "Cozinhar no vapor", "tempoPreparoMinutos": 40},
            "embalagem": {
                "tipo": "PLASTICA", "descricao": "Plástica simples",
                "custo": 0.50, "pesoSuportado": 0.5,
                "tipoPlastico": "Polietileno", "reciclavel": true
            },
            "categorias": [{"nome": "Doces", "descricao": "Pamonhas doces"}],
            "itensReceita": [{"quantidade": 2.0, "unidadeMedida": "KG", "ingredienteId": 1}]
        }
        """;

    @InjectMock
    PamonhaService pamonhaService;

    @BeforeEach
    void setUp() {
        reset(pamonhaService);
    }

    // -------------------------------------------------------------------------
    // GET /pamonhas
    // -------------------------------------------------------------------------

    @Test
    void deveListarPamonhasComStatus200() {
        when(pamonhaService.findAll(anyInt(), anyInt())).thenReturn(List.of(
            pamonha(1L, "Pamonha Doce Tradicional", "Pamonha doce com massa de milho", BigDecimal.valueOf(8.50), 50),
            pamonha(2L, "Pamonha Salgada com Queijo", "Pamonha salgada com queijo", BigDecimal.valueOf(10.00), 30)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Pamonha Doce Tradicional"))
            .body("[0].preco", equalTo(8.50F));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(pamonhaService.findAll(anyInt(), anyInt())).thenReturn(List.of());

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
        when(pamonhaService.findAll(0, 5)).thenReturn(List.of(
            pamonha(1L, "Pamonha Doce Tradicional", "Pamonha doce", BigDecimal.valueOf(8.50), 50)
        ));

        given()
            .accept(ContentType.JSON)
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200);

        verify(pamonhaService).findAll(0, 5);
    }

    // -------------------------------------------------------------------------
    // GET /pamonhas/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(pamonhaService.findById(1L))
            .thenReturn(pamonha(1L, "Pamonha Doce Tradicional", "Pamonha doce", BigDecimal.valueOf(8.50), 50));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("nome", equalTo("Pamonha Doce Tradicional"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(pamonhaService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // GET /pamonhas/find/nome/{nome}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorNomeComStatus200() {
        when(pamonhaService.findByNome("Doce")).thenReturn(List.of(
            pamonha(1L, "Pamonha Doce Tradicional", "Pamonha doce", BigDecimal.valueOf(8.50), 50)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/nome/Doce")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].nome", equalTo("Pamonha Doce Tradicional"));
    }

    @Test
    void deveBuscarPorNomeRetornarListaVazia() {
        when(pamonhaService.findByNome("Inexistente")).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/nome/Inexistente")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // GET /pamonhas/find/categoria/{categoriaId}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorCategoriaComStatus200() {
        when(pamonhaService.findByCategoria(3L)).thenReturn(List.of(
            pamonha(1L, "Pamonha Doce Tradicional", "Pamonha doce", BigDecimal.valueOf(8.50), 50)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/categoria/3")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarPorCategoriaRetornarListaVazia() {
        when(pamonhaService.findByCategoria(99L)).thenReturn(List.of());

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/categoria/99")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    // -------------------------------------------------------------------------
    // POST /pamonhas
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarPamonhaComStatus201() {
        PamonhaResponseDTO response = new PamonhaResponseDTO(
            10L, "Pamonha Doce Tradicional", "Pamonha doce com massa de milho",
            BigDecimal.valueOf(8.50), 50, null, null, null, null, null
        );
        when(pamonhaService.create(any())).thenReturn(response);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(PAYLOAD_VALIDO)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Pamonha Doce Tradicional"))
            .body("preco", equalTo(8.50F));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(PAYLOAD_VALIDO)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(pamonhaService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoNomeEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"descricao\":\"Pamonha doce\",\"preco\":8.50,\"estoque\":50}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(pamonhaService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoPrecoNulo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Pamonha Doce\",\"descricao\":\"Pamonha doce\",\"preco\":null,\"estoque\":50}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(pamonhaService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar400QuandoJsonForMalformado() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Pamonha Doce\",\"descricao\":\"Pamonha\"")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(400);
    }

    // -------------------------------------------------------------------------
    // PUT /pamonhas/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarPamonhaComStatus204() {
        doNothing().when(pamonhaService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(PAYLOAD_VALIDO)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(pamonhaService).update(any(Long.class), any());
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(PAYLOAD_VALIDO)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(pamonhaService, never()).update(any(), any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("nome", "Já existe uma pamonha com este nome"))
            .when(pamonhaService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(PAYLOAD_VALIDO)
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
    // DELETE /pamonhas/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRemoverPamonhaComStatus204() {
        doNothing().when(pamonhaService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(pamonhaService).delete(1L);
    }

    @Test
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(pamonhaService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Pamonha pamonha(Long id, String nome, String descricao, BigDecimal preco, int estoque) {
        Pamonha p = new Pamonha();
        p.setId(id);
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setEstoque(estoque);
        return p;
    }
}
