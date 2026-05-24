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
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import br.unitins.tp1.projeto.service.EmbalagemService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class EmbalagemResourceHttpContractTest {

    private static final String BASE_URL = "/embalagens";

    @InjectMock
    EmbalagemService embalagemService;

    @BeforeEach
    void setUp() {
        reset(embalagemService);
    }

    // -------------------------------------------------------------------------
    // GET /embalagens
    // -------------------------------------------------------------------------

    @Test
    void deveListarEmbalagensComStatus200() {
        when(embalagemService.findAll(anyInt(), anyInt())).thenReturn(List.of(
            plastica(1L, "Embalagem plástica simples", BigDecimal.valueOf(0.50), 0.5),
            biodegradavel(2L, "Embalagem biodegradável premium", BigDecimal.valueOf(1.20), 0.7)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].descricao", equalTo("Embalagem plástica simples"))
            .body("[0].tipo", equalTo("PLASTICA"));
    }

    @Test
    void deveRetornarListaVaziaComStatus200() {
        when(embalagemService.findAll(anyInt(), anyInt())).thenReturn(List.of());

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
        when(embalagemService.findAll(0, 2)).thenReturn(List.of(
            plastica(1L, "Embalagem plástica simples", BigDecimal.valueOf(0.50), 0.5)
        ));

        given()
            .accept(ContentType.JSON)
            .queryParam("page", 0)
            .queryParam("size", 2)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200);

        verify(embalagemService).findAll(0, 2);
    }

    // -------------------------------------------------------------------------
    // GET /embalagens/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorIdComStatus200() {
        when(embalagemService.findById(1L))
            .thenReturn(plastica(1L, "Embalagem plástica simples", BigDecimal.valueOf(0.50), 0.5));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("descricao", equalTo("Embalagem plástica simples"))
            .body("tipo", equalTo("PLASTICA"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(embalagemService.findById(999L)).thenReturn(null);

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    // -------------------------------------------------------------------------
    // GET /embalagens/find/tipo/{tipo}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarPorTipoPlasticaComStatus200() {
        when(embalagemService.findByTipo("PLASTICA")).thenReturn(List.of(
            plastica(1L, "Embalagem plástica simples", BigDecimal.valueOf(0.50), 0.5)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/tipo/PLASTICA")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].tipo", equalTo("PLASTICA"));
    }

    @Test
    void deveBuscarPorTipoBiodegradavelComStatus200() {
        when(embalagemService.findByTipo("BIODEGRADAVEL")).thenReturn(List.of(
            biodegradavel(2L, "Embalagem biodegradável premium", BigDecimal.valueOf(1.20), 0.7)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/tipo/BIODEGRADAVEL")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].tipo", equalTo("BIODEGRADAVEL"));
    }

    // -------------------------------------------------------------------------
    // GET /embalagens/order/custo
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarOrdenadasPorCustoComStatus200() {
        when(embalagemService.findOrderByCusto()).thenReturn(List.of(
            plastica(1L, "Plástica barata", BigDecimal.valueOf(0.50), 0.5),
            biodegradavel(2L, "Biodegradável cara", BigDecimal.valueOf(1.20), 0.7)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/order/custo")
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    // -------------------------------------------------------------------------
    // GET /embalagens/find/biodegradavel/{dias}
    // -------------------------------------------------------------------------

    @Test
    void deveBuscarBiodegradavelRapidaComStatus200() {
        when(embalagemService.findBiodegradavelRapida(60)).thenReturn(List.of(
            biodegradavel(2L, "Papel vegetal", BigDecimal.valueOf(1.20), 0.7)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/biodegradavel/60")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].tipo", equalTo("BIODEGRADAVEL"));
    }

    // -------------------------------------------------------------------------
    // POST /embalagens
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarEmbalagemPlasticaComStatus201() {
        when(embalagemService.create(any()))
            .thenReturn(plastica(10L, "Polietileno", BigDecimal.valueOf(0.50), 0.5));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Polietileno\",\"custo\":0.50,\"pesoSuportado\":0.5,\"tipoPlastico\":\"Polietileno\",\"reciclavel\":true}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("tipo", equalTo("PLASTICA"));
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveCriarEmbalagemBiodegradavelComStatus201() {
        when(embalagemService.create(any()))
            .thenReturn(biodegradavel(11L, "Papel vegetal", BigDecimal.valueOf(1.20), 0.7));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"BIODEGRADAVEL\",\"descricao\":\"Papel vegetal\",\"custo\":1.20,\"pesoSuportado\":0.7,\"material\":\"Papel\",\"tempoDecomposicaoDias\":90}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(11))
            .body("tipo", equalTo("BIODEGRADAVEL"));
    }

    @Test
    void deveRetornar401AoCriarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica\",\"custo\":0.50,\"pesoSuportado\":0.5,\"tipoPlastico\":\"PVC\",\"reciclavel\":true}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(401);

        verify(embalagemService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoTipoEmBranco() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"\",\"descricao\":\"Plástica\",\"custo\":0.50,\"pesoSuportado\":0.5}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(embalagemService, never()).create(any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRetornar422QuandoCustoNegativo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica\",\"custo\":-1.0,\"pesoSuportado\":0.5,\"tipoPlastico\":\"PVC\",\"reciclavel\":true}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(embalagemService, never()).create(any());
    }

    // -------------------------------------------------------------------------
    // PUT /embalagens/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveAtualizarEmbalagemComStatus204() {
        doNothing().when(embalagemService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica atualizada\",\"custo\":0.60,\"pesoSuportado\":0.6,\"tipoPlastico\":\"PET\",\"reciclavel\":true}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(embalagemService).update(any(Long.class), any());
    }

    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica\",\"custo\":0.50,\"pesoSuportado\":0.5,\"tipoPlastico\":\"PVC\",\"reciclavel\":true}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(embalagemService, never()).update(any(), any());
    }

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("descricao", "Já existe uma embalagem com esta descrição"))
            .when(embalagemService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica PVC\",\"custo\":0.50,\"pesoSuportado\":0.5,\"tipoPlastico\":\"PVC\",\"reciclavel\":true}")
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
    // DELETE /embalagens/{id}
    // -------------------------------------------------------------------------

    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @Test
    void deveRemoverEmbalagemComStatus204() {
        doNothing().when(embalagemService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(embalagemService).delete(1L);
    }

    @Test
    void deveRetornar401AoRemoverSemAutenticacao() {
        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(401);

        verify(embalagemService, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private EmbalagemPlastica plastica(Long id, String descricao, BigDecimal custo, double peso) {
        EmbalagemPlastica e = new EmbalagemPlastica();
        e.setId(id);
        e.setDescricao(descricao);
        e.setCusto(custo);
        e.setPesoSuportado(peso);
        e.setTipoPlastico("Polietileno");
        e.setReciclavel(true);
        return e;
    }

    private EmbalagemBiodegradavel biodegradavel(Long id, String descricao, BigDecimal custo, double peso) {
        EmbalagemBiodegradavel e = new EmbalagemBiodegradavel();
        e.setId(id);
        e.setDescricao(descricao);
        e.setCusto(custo);
        e.setPesoSuportado(peso);
        e.setMaterial("Papel vegetal");
        e.setTempoDecomposicaoDias(90);
        return e;
    }
}
