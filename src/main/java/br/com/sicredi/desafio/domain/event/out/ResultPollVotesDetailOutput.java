package br.com.sicredi.desafio.domain.event.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ResultPollVotesDetailOutput {

    @JsonProperty("paulta")
    private String poll;

    @JsonProperty("quantidadeVoto")
    private Integer countVotes;

    @JsonProperty("perguntas")
    private Map<String, Integer> questions;

}
