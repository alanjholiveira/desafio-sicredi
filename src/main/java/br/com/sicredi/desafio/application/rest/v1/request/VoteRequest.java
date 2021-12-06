package br.com.sicredi.desafio.application.rest.v1.request;

import br.com.sicredi.desafio.infrastructure.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotNull
    private UUID associate;

    @NotNull
    private VoteType vote;

    @NotNull
    private UUID session;

}
