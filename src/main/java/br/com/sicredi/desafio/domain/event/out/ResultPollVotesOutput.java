package br.com.sicredi.desafio.domain.event.out;


import br.com.sicredi.desafio.domain.entity.Result;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultPollVotesOutput {

    @JsonProperty("CURRENT_VALUES")
    private ResultPollVotesDetailOutput currentValues;

    @JsonProperty("ORIGINAL_VALUES")
    private ResultPollVotesDetailOutput originalValues;

    public static ResultPollVotesOutput create(Result result) {
        ResultPollVotesDetailOutput detail = ResultPollVotesDetailOutput.builder()
                .poll(result.getPoll().getName())
                .countVotes(result.getCountVotes())
                .questions(result.getQuestions())
                .build();

        return ResultPollVotesOutput.builder()
                .currentValues(detail)
                .originalValues(detail)
                .build();
    }
}
