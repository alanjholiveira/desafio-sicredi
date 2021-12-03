package br.com.sicredi.desafio.application.mapper;

import br.com.sicredi.desafio.application.rest.v1.response.ResultResponse;
import br.com.sicredi.desafio.domain.entity.Result;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultMapper {

    public static ResultResponse toResponse(Result result) {
        return ResultResponse.builder()
                .countVotes(result.getCountVotes())
                .poll(result.getPoll().getName())
                .questions(result.getQuestions())
                .build();
    }

}
