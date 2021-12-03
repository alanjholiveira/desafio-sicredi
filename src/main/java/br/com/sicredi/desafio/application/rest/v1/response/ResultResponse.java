package br.com.sicredi.desafio.application.rest.v1.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {

    private String poll;
    private long countVotes;
    private Map<String, Integer> questions;

}
