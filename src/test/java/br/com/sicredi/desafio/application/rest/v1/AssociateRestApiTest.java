package br.com.sicredi.desafio.application.rest.v1;

import br.com.sicredi.desafio.application.mapper.AssociateMapper;
import br.com.sicredi.desafio.application.rest.v1.request.AssociateRequest;
import br.com.sicredi.desafio.builder.entity.AssociateBuilder;
import br.com.sicredi.desafio.domain.entity.Associate;
import br.com.sicredi.desafio.domain.service.AssociateService;
import br.com.sicredi.desafio.infrastructure.client.AssociateStatusClient;
import br.com.sicredi.desafio.infrastructure.client.response.StatusResponse;
import br.com.sicredi.desafio.infrastructure.enums.AssociateStatus;
import br.com.sicredi.desafio.infrastructure.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

@SpringBootTest
class AssociateRestApiTest {

    private static final String URL = "/v1/associates";

    @Autowired
    private AssociateRestApi associateRestApi;

    @Autowired
    private AssociateBuilder builder;

    @Mock
    private AssociateService service;

    @Mock
    private AssociateStatusClient statusClient;

    @BeforeEach
    public void setup() {
        standaloneSetup(this.associateRestApi);
    }

    @Test
    void when_getAll_returns_success() throws ParseException {
        Associate associate = builder.construirEntidade();

        when(service.findAll()).thenReturn(List.of(associate));

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL)
        .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    void when_create_associate_return_sucess() throws IOException, ParseException {
        AssociateRequest associateRequest = AssociateRequest.builder()
                        .name("Novo Associado")
                        .taxId("96979542087")
                        .build();
        Associate associate = builder.construirEntidade();

        when(service.save(AssociateMapper.toEntity(associateRequest))).thenReturn(associate);
        when(statusClient.getStatus(associate.getTaxId()))
                .thenReturn(StatusResponse.builder().status(AssociateStatus.ABLE_TO_VOTE).build());

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType("application/json")
                .body(TestUtil.convertObjectToJsonBytes(associateRequest))
        .when()
                .post(URL)
        .then()
                .statusCode(HttpStatus.CREATED.value());

    }

}
