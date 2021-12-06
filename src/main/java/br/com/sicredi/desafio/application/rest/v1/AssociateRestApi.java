package br.com.sicredi.desafio.application.rest.v1;

import br.com.sicredi.desafio.application.mapper.AssociateMapper;
import br.com.sicredi.desafio.application.rest.v1.request.AssociateRequest;
import br.com.sicredi.desafio.application.rest.v1.response.AssociateResponse;
import br.com.sicredi.desafio.domain.entity.Associate;
import br.com.sicredi.desafio.domain.service.AssociateService;
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
@Api(tags = "Associado", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("v1/associates")
@RestController
@AllArgsConstructor
@Validated
public class AssociateRestApi {

    private AssociateService service;

    @ApiOperation(value = "API list all Associate", response = AssociateResponse.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<AssociateResponse>> getAll() {
        log.info("Receiving request to fetch all members.");
        List<AssociateResponse> responseList = service.findAll().stream()
                .map(AssociateMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @ApiOperation(value = "API to Create Associate", response = AssociateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<AssociateResponse> createAssociate(@RequestBody @Valid AssociateRequest request) {
        log.info("Receiving a request to register a new member. {}", request);
        Associate associate = service.save(AssociateMapper.toEntity(request));

        return new ResponseEntity<>(AssociateMapper.toResponse(associate), HttpStatus.CREATED);
    }

}
