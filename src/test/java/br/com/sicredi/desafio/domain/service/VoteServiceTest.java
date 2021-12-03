package br.com.sicredi.desafio.domain.service;

import br.com.sicredi.desafio.builder.entity.AssociateBuilder;
import br.com.sicredi.desafio.builder.entity.SessionBuilder;
import br.com.sicredi.desafio.builder.entity.VoteBuilder;
import br.com.sicredi.desafio.domain.entity.Associate;
import br.com.sicredi.desafio.domain.entity.Result;
import br.com.sicredi.desafio.domain.entity.Session;
import br.com.sicredi.desafio.domain.entity.Vote;
import br.com.sicredi.desafio.infrastructure.enums.SessionStatus;
import br.com.sicredi.desafio.infrastructure.enums.VoteType;
import br.com.sicredi.desafio.infrastructure.producer.ResultPollVotesProducer;
import br.com.sicredi.desafio.infrastructure.repository.AssociateRepository;
import br.com.sicredi.desafio.infrastructure.repository.SessionRepository;
import br.com.sicredi.desafio.infrastructure.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository repository;

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ResultPollVotesProducer producerEvent;

    @Autowired
    private AssociateBuilder associateBuilder;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Autowired
    private VoteBuilder builder;

    @Test
    void when_vote_returns_success() throws ParseException {
        Vote voteBuilder = builder.construirEntidade();
        Associate associate = associateBuilder.construirEntidade();
        Session session = sessionBuilder.construirEntidade();
        when(repository.existsVoteByAssociateAndSession(associate, session))
                .thenReturn(Boolean.FALSE);
        when(sessionRepository.findById(voteBuilder.getSession().getId()))
                .thenReturn(Optional.of(session));
        when(associateRepository.findById(voteBuilder.getAssociate().getId()))
                .thenReturn(Optional.of(associate));
        when(repository.save(voteBuilder)).thenReturn(voteBuilder);

        String response = service.vote(voteBuilder);

        assertNotNull(response);
        assertEquals("Vote registered successfully.", response);
    }

    @Test
    void when_count_votes_session_return_success() throws ParseException {
        Vote voteBuilder = builder.construirEntidade();
        Session session = sessionBuilder.construirEntidade();
        session.setExpiration(session.getCreatedAt());
        session.setVotes(List.of(voteBuilder));
        when(sessionRepository.findById(voteBuilder.getSession().getId()))
                .thenReturn(Optional.of(session));

        Result response = service.countVotes(voteBuilder.getSession().getId().toString());
        assertNotNull(response);
    }

    @Test
    void when_counting_votes_session_return_success() throws ParseException {
        Vote voteBuilder = builder.construirEntidade();
        Session session = sessionBuilder.construirEntidade();
        session.setExpiration(session.getCreatedAt());
        session.setVotes(List.of(voteBuilder));
        when(sessionRepository.findByStatus(SessionStatus.OPEN))
                .thenReturn(List.of(session));

        Result result = Result.builder()
                        .countVotes(session.getVotes().size())
                        .poll(session.getPoll())
                        .questions(questions(session))
                        .build();

        service.countingVotesEvent();

        producerEvent.send(result);

        verify(producerEvent).send(isA(Result.class));
    }

    private Map<String, Integer> questions(Session session) {
        return Arrays.stream(VoteType.values())
                .collect(Collectors.toMap(VoteType::name, v -> getVotesByType(session, v).size()));
    }

    private List<Vote> getVotesByType(Session session, VoteType voteType) {
        return session.getVotes().stream().filter(v -> voteType.equals(v.getVoteType())).collect(Collectors.toList());
    }
}
