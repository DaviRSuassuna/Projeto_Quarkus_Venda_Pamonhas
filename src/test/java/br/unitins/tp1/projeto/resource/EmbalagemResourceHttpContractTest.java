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
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import br.unitins.tp1.projeto.service.EmbalagemService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class EmbalagemResourceHttpContractTest {

    private static final String BASE_URL = "/embalagens";

    @InjectMock
    EmbalagemService embalagemService;

    @BeforeEach
    void setUp() {
        reset(embalagemService);
    }

    @Test
    void deveListarEmbalagenComStatus200() {
        when(embalagemService.findAll()).thenReturn(List.of(
            embalagemPlastica(1L, "Plástica PVC", BigDecimal.valueOf(10.0), 5.0)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(1))
            .body("[0].id", equalTo(1))
            .body("[0].descricao", equalTo("Plástica PVC"));
    }

    @Test
    void deveBuscarPorIdComStatus200() {
        when(embalagemService.findById(1L))
            .thenReturn(embalagemPlastica(1L, "Plástica PVC", BigDecimal.valueOf(10.0), 5.0));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("descricao", equalTo("Plástica PVC"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(embalagemService.findById(999L))
            .thenThrow(new NotFoundException("Embalagem não encontrada"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarEmbalagemPlasticaComStatus201() {
        when(embalagemService.create(any()))
            .thenReturn(embalagemPlastica(10L, "Plástica PVC", BigDecimal.valueOf(10.0), 5.0));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica PVC\",\"custo\":10.0,\"pesoSuportado\":5.0,\"tipoPlastico\":\"PVC\",\"reciclavel\":true}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("descricao", equalTo("Plástica PVC"));
    }

    @Test
    void deveCriarEmbalagemBiodegradavelComStatus201() {
        when(embalagemService.create(any()))
            .thenReturn(embalagemPlastica(11L, "Papel Biodegradável", BigDecimal.valueOf(15.0), 3.0));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"BIODEGRADAVEL\",\"descricao\":\"Papel Biodegradável\",\"custo\":15.0,\"pesoSuportado\":3.0,\"material\":\"Papel\",\"tempoDecomposicaoDias\":30}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(11))
            .body("descricao", equalTo("Papel Biodegradável"));
    }

    @Test
    void deveAtualizarEmbalagemComStatus204() {
        doNothing().when(embalagemService).update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica PET atualizada\",\"custo\":12.0,\"pesoSuportado\":6.0,\"tipoPlastico\":\"PET\",\"reciclavel\":true}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(embalagemService).update(any(Long.class), any());
    }

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
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"\",\"descricao\":\"\",\"custo\":-1.0,\"pesoSuportado\":-5.0}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(embalagemService, never()).create(any());
    }

    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("descricao", "Já existe uma embalagem com esta descrição"))
            .when(embalagemService)
            .update(any(Long.class), any());

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"tipo\":\"PLASTICA\",\"descricao\":\"Plástica PVC\",\"custo\":10.0,\"pesoSuportado\":5.0,\"tipoPlastico\":\"PVC\",\"reciclavel\":true}")
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

    private EmbalagemPlastica embalagemPlastica(Long id, String descricao, BigDecimal custo, double pesoSuportado) {
        EmbalagemPlastica emb = new EmbalagemPlastica();
        emb.setId(id);
        emb.setDescricao(descricao);
        emb.setCusto(custo);
        emb.setPesoSuportado(pesoSuportado);
        emb.setTipoPlastico("PVC");
        emb.setReciclavel(true);
        return emb;
    }
}
