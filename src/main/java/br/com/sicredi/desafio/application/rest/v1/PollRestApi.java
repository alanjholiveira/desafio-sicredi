package br.com.sicredi.desafio.application.rest.v1;

import br.com.sicredi.desafio.application.mapper.PollMapper;
import br.com.sicredi.desafio.application.rest.v1.request.PollRequest;
import br.com.sicredi.desafio.application.rest.v1.response.PollResponse;
import br.com.sicredi.desafio.domain.entity.Poll;
import br.com.sicredi.desafio.domain.service.PollService;
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
@Api(tags = "Pauta", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("v1/polls")
@RestController
@AllArgsConstructor
@Validated
public class PollRestApi {

    private PollService service;

    @ApiOperation(value = "API list all Poll", response = PollResponse.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<PollResponse>> getAll() {
        log.info("Receiving request to search all poll");
        List<PollResponse> responseList = service.findAll().stream()
                .map(PollMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @ApiOperation(value = "API to Create Poll", response = PollResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
                            @ApiResponse(code = 400, message = "Bad Request"),
                            @ApiResponse(code = 401, message = "Unauthorized"),
                            @ApiResponse(code = 422, message = "Unprocessable Entity"),
                            @ApiResponse(code = 500, message = "Internal Server Error") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@RequestBody @Valid PollRequest request) {
        log.info("Receiving a request to register a new poll. {}", request);
        Poll poll = service.save(PollMapper.toEntity(request));

        return new ResponseEntity<>(PollMapper.toResponse(poll), HttpStatus.CREATED);
    }

}
