package br.com.sicredi.desafio.application.rest.v1.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SessionRequest {

    @NotNull
    private UUID pollId;

    private LocalDateTime expiration;

}
