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

import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.service.PamonhaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class PamonhaResourceHttpContractTest {

    private static final String BASE_URL = "/pamonhas";

    @InjectMock
    PamonhaService pamonhaService;

    @BeforeEach
    void setUp() {
        reset(pamonhaService);
    }

    @Test
    void deveListarPamonhasComStatus200() {
        when(pamonhaService.findAll()).thenReturn(List.of(
            pamonha(1L, "Pamonha Doce", "Pamonha doce tradicional"),
            pamonha(2L, "Pamonha Salgada", "Pamonha salgada tradicional")
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
            .body("[0].nome", equalTo("Pamonha Doce"));
    }

    @Test
    void deveBuscarPorIdComStatus200() {
        when(pamonhaService.findById(1L))
            .thenReturn(pamonha(1L, "Pamonha Doce", "Pamonha doce tradicional"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Pamonha Doce"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorIdInexistente() {
        when(pamonhaService.findById(999L))
            .thenThrow(new NotFoundException("Pamonha não encontrada"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarPamonhaComStatus201() {
        PamonhaResponseDTO response = new PamonhaResponseDTO(
            10L, "Pamonha Doce", "Pamonha doce tradicional",
            BigDecimal.valueOf(5.0), 10, null, null, null, null, null
        );
        when(pamonhaService.create(any()))
            .thenReturn(response);

        String payload = """
            {
                "nome":"Pamonha Doce",
                "descricao":"Pamonha doce tradicional",
                "preco":5.0,
                "estoque":10,
                "tabelaNutricional":{"valorEnergetico":100,"carboidratos":20,"proteinas":5,"gordurasTotais":2,"fibras":1,"sodio":0.5},
                "modoPreparo":{"descricao":"Cozinhar em vapor","tempoPreparoMinutos":30},
                "embalagem":{"tipo":"PLASTICA","descricao":"Plástica","custo":1.0,"pesoSuportado":5.0,"tipoPlastico":"PVC","reciclavel":true},
                "categorias":[{"nome":"Doce","descricao":"Pães doces"}],
                "itensReceita":[{"quantidade":1.0,"unidadeMedida":"KG","ingredienteId":1}]
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(payload)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Pamonha Doce"));
    }

    @Test
    void deveAtualizarPamonhaComStatus204() {
        doNothing().when(pamonhaService).update(any(Long.class), any());

        String payload = """
            {
                "nome":"Pamonha Doce Premium",
                "descricao":"Pamonha doce premium",
                "preco":7.0,
                "estoque":15,
                "tabelaNutricional":{"valorEnergetico":100,"carboidratos":20,"proteinas":5,"gordurasTotais":2,"fibras":1,"sodio":0.5},
                "modoPreparo":{"descricao":"Cozinhar em vapor","tempoPreparoMinutos":30},
                "embalagem":{"tipo":"PLASTICA","descricao":"Plástica Premium","custo":1.5,"pesoSuportado":5.0,"tipoPlastico":"PET","reciclavel":true},
                "categorias":[{"nome":"Doce","descricao":"Pães doces"}],
                "itensReceita":[{"quantidade":1.0,"unidadeMedida":"KG","ingredienteId":1}]
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(payload)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(204);

        verify(pamonhaService).update(any(Long.class), any());
    }

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
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"descricao\":\"\",\"preco\":-1.0,\"estoque\":-10}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(pamonhaService, never()).create(any());
    }

    @Test
    void deveMapearValidationExceptionCorretamente() {
        doThrow(new ValidationException("nome", "Já existe uma pamonha com este nome"))
            .when(pamonhaService)
            .update(any(Long.class), any());

        String payload = """
            {
                "nome":"Pamonha Doce",
                "descricao":"Pamonha doce tradicional",
                "preco":5.0,
                "estoque":10,
                "tabelaNutricional":{"valorEnergetico":100,"carboidratos":20,"proteinas":5,"gordurasTotais":2,"fibras":1,"sodio":0.5},
                "modoPreparo":{"descricao":"Cozinhar em vapor","tempoPreparoMinutos":30},
                "embalagem":{"tipo":"PLASTICA","descricao":"Plástica","custo":1.0,"pesoSuportado":5.0,"tipoPlastico":"PVC","reciclavel":true},
                "categorias":[{"nome":"Doce","descricao":"Pães doces"}],
                "itensReceita":[{"quantidade":1.0,"unidadeMedida":"KG","ingredienteId":1}]
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(payload)
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

    private Pamonha pamonha(Long id, String nome, String descricao) {
        Pamonha p = new Pamonha();
        p.setId(id);
        p.setNome(nome);
        p.setDescricao(descricao);
        return p;
    }
}
