package br.com.sicredi.desafio.application.rest.v1;

import br.com.sicredi.desafio.application.mapper.SessionMapper;
import br.com.sicredi.desafio.application.rest.v1.request.SessionRequest;
import br.com.sicredi.desafio.builder.entity.PollBuilder;
import br.com.sicredi.desafio.builder.entity.SessionBuilder;
import br.com.sicredi.desafio.domain.entity.Poll;
import br.com.sicredi.desafio.domain.entity.Session;
import br.com.sicredi.desafio.domain.service.SessionService;
import br.com.sicredi.desafio.infrastructure.enums.SessionStatus;
import br.com.sicredi.desafio.infrastructure.repository.PollRepository;
import br.com.sicredi.desafio.infrastructure.repository.SessionRepository;
import br.com.sicredi.desafio.infrastructure.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class SessionRestApiTest {

    private static final String URL = "/v1/sessions";

    @Autowired
    private SessionRestApi restApi;

    @Autowired
    private SessionBuilder builder;

    @Autowired
    private PollBuilder pollBuilder;

    @Mock
    private SessionService service;

    @MockBean
    private PollRepository pollRepository;

    @BeforeEach
    public void setup() {
        standaloneSetup(this.restApi);
    }

    @Test
    void when_get_all_returns_success() throws ParseException {
        Session entity = builder.construirEntidade();

        when(service.findAll()).thenReturn(List.of(entity));

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL)
        .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    void when_open_session_return_sucess() throws IOException, ParseException {
        Poll poll = pollBuilder.construirEntidade();
        poll.setId(UUID.fromString("7916bf40-03c1-4165-8727-958765dc02c1"));
        Session session = builder.construirEntidade();

        SessionRequest request = SessionRequest.builder()
                .pollId(poll.getId())
                .expiration(session.getExpiration())
                .build();

        when(pollRepository.findById(poll.getId())).thenReturn(Optional.of(poll));
        when(service.openSession(SessionMapper.toEntity(request))).thenReturn(session);


        given()
            .accept(MediaType.APPLICATION_JSON)
            .contentType("application/json")
            .body(TestUtil.convertObjectToJsonBytes(request))
        .when()
                .post(URL)
        .then()
                .statusCode(HttpStatus.CREATED.value());

    }

}
