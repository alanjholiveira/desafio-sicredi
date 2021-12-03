package br.com.sicredi.desafio.application.mapper;

import br.com.sicredi.desafio.application.rest.v1.request.VoteRequest;
import br.com.sicredi.desafio.application.rest.v1.response.VoteResponse;
import br.com.sicredi.desafio.domain.entity.Associate;
import br.com.sicredi.desafio.domain.entity.Session;
import br.com.sicredi.desafio.domain.entity.Vote;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VoteMapper {

    public static Vote toEntity(VoteRequest request) {
        return Vote.builder()
                .session(Session.builder().id(request.getSession()).build())
                .associate(Associate.builder().id(request.getAssociate()).build())
                .voteType(request.getVote())
                .build();
    }

    public static VoteResponse toResponse(String message) {
        return VoteResponse.builder()
                .message(message)
                .build();
    }

}
