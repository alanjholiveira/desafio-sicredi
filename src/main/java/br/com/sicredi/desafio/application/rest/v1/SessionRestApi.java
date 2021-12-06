package br.com.sicredi.desafio.application.rest.v1;

import br.com.sicredi.desafio.application.mapper.SessionMapper;
import br.com.sicredi.desafio.application.rest.v1.request.SessionRequest;
import br.com.sicredi.desafio.application.rest.v1.response.SessionResponse;
import br.com.sicredi.desafio.domain.entity.Session;
import br.com.sicredi.desafio.domain.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "Sessão", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("v1/sessions")
@RestController
@AllArgsConstructor
@Validated
public class SessionRestApi {

    private final SessionService service;

    @ApiOperation(value = "API List of all Session", notes = "Search All Registered Sessions",
            response = SessionResponse.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAll() {
        log.info("Receiving request to fetch all session.");
        List<SessionResponse> responseList = service.findAll().stream()
                .map(SessionMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @ApiOperation(value = "API Open Session", notes = "Open Session for Poll Voting", response = SessionResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SessionResponse> openSession(@RequestBody @Valid SessionRequest request) {
        log.info("Receiving session opening request. {}", request);
        Session session = service.openSession(SessionMapper.toEntity(request));

        return new ResponseEntity<>(SessionMapper.toResponse(session), HttpStatus.CREATED);
    }


}
