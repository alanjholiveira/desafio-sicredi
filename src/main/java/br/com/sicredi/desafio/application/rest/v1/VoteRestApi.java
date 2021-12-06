package br.com.sicredi.desafio.application.rest.v1;

import br.com.sicredi.desafio.application.mapper.ResultMapper;
import br.com.sicredi.desafio.application.mapper.VoteMapper;
import br.com.sicredi.desafio.application.rest.v1.request.VoteRequest;
import br.com.sicredi.desafio.application.rest.v1.response.ResultResponse;
import br.com.sicredi.desafio.application.rest.v1.response.VoteResponse;
import br.com.sicredi.desafio.domain.entity.Result;
import br.com.sicredi.desafio.domain.service.VoteService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "Votação", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("v1/votes")
@RestController
@AllArgsConstructor
@Validated
public class VoteRestApi {

    private final VoteService service;

    @PostMapping
    public ResponseEntity<VoteResponse> vote(@RequestBody @Valid VoteRequest request) {
        log.info("Receiving request to compute vote. {}", request.getSession());
        String message = service.vote(VoteMapper.toEntity(request));

        return ResponseEntity.ok(VoteMapper.toResponse(message));

    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ResultResponse> countVotes(@PathVariable String sessionId) {
        log.info("Receiving request to post result. {}", sessionId);
        Result result = service.countVotes(sessionId);
        return ResponseEntity.ok(ResultMapper.toResponse(result));
    }

}
