package br.com.sicredi.desafio.application.mapper;

import br.com.sicredi.desafio.application.rest.v1.request.PollRequest;
import br.com.sicredi.desafio.application.rest.v1.response.PollResponse;
import br.com.sicredi.desafio.domain.entity.Poll;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PollMapper {

    public static PollResponse toResponse(Poll entity) {
        return PollResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static Poll toEntity(PollRequest request) {
        return Poll.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

}
