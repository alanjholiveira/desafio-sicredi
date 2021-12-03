package br.com.sicredi.desafio.domain.service;

import br.com.sicredi.desafio.domain.entity.Associate;
import br.com.sicredi.desafio.domain.entity.Result;
import br.com.sicredi.desafio.domain.entity.Session;
import br.com.sicredi.desafio.domain.entity.Vote;
import br.com.sicredi.desafio.infrastructure.enums.AssociateStatus;
import br.com.sicredi.desafio.infrastructure.enums.SessionStatus;
import br.com.sicredi.desafio.infrastructure.enums.VoteType;
import br.com.sicredi.desafio.infrastructure.exception.*;
import br.com.sicredi.desafio.infrastructure.producer.ResultPollVotesProducer;
import br.com.sicredi.desafio.infrastructure.repository.AssociateRepository;
import br.com.sicredi.desafio.infrastructure.repository.SessionRepository;
import br.com.sicredi.desafio.infrastructure.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final AssociateRepository associateRepository;
    private final SessionRepository sessionRepository;
    private final ResultPollVotesProducer producerEvent;

    public String vote(Vote entity) {
        log.info("Computing vote. Session: {}", entity.getSession());
        Boolean isVote = isVote(entity.getAssociate(), entity.getSession());
        Boolean openSession = getOpenSession(entity.getSession().getId()).isOpenSession();
        Associate associate = getAssociate(entity);

        if (openSession.equals(Boolean.FALSE)) {
            log.error("It is not possible to compute the vote with Session closed.");
            throw new VotingClosedException();
        }

        if (associate.getStatus().equals(AssociateStatus.UNABLE_TO_VOTE)) {
            log.error("Member not authorized to vote.");
            throw new AssociateUnableToVoteException();
        }

        if (isVote.equals(Boolean.TRUE)) {
            log.error("Member has already voted in this session.");
            throw new AssociateVoteUniqueException();
        }

        repository.save(entity);
        log.info("Vote successfully tallied.");

        return "Vote registered successfully.";

    }

    public Result countVotes(String sessionId) {
        log.info("Checking if the poll is closed. Session: {}", sessionId);
        Session session = getOpenSession(UUID.fromString(sessionId));

        if (session.isOpenSession().equals(Boolean.TRUE)) {
            log.error("It is not possible to count votes from the poll with an open session. Session: {}", sessionId);
            throw new SessionNotCountVoteException();
        }
        log.info("Votes successfully counted. Session: {}", sessionId);
        return getResultBuild(session);

    }

    @Transactional
    public void countingVotesEvent() {
        log.info("Starting vote counting");
        getCloseSession().stream()
                .map(this::getResultBuild)
                .forEach(producerEvent::send);
    }

    private List<Session> getCloseSession() {
        List<Session> sessions = sessionRepository.findByStatus(SessionStatus.OPEN).parallelStream()
                .filter(session -> session.isOpenSession().equals(Boolean.FALSE))
                .map(session -> {
                    session.setStatus(SessionStatus.CLOSED);
                    return session;
                }).collect(Collectors.toList());

        return sessionRepository.saveAll(sessions);
    }

    private Result getResultBuild(Session session) {
        return Result.builder()
                .poll(session.getPoll())
                .countVotes(session.getVotes().size())
                .questions(questions(session))
                .build();
    }

    private Map<String, Integer> questions(Session session) {
        return Arrays.stream(VoteType.values())
                .collect(Collectors.toMap(VoteType::name, v -> getVotesByType(session, v).size()));
    }

    private List<Vote> getVotesByType(Session session, VoteType voteType) {
        return session.getVotes().stream().filter(v -> voteType.equals(v.getVoteType())).collect(Collectors.toList());
    }

    private Associate getAssociate(Vote entity) {
        return associateRepository.findById(entity.getAssociate().getId())
                .orElseThrow(AssociateNotFoundException::new);
    }

    private Session getOpenSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(SessionNotFoundException::new);
    }

    private Boolean isVote(Associate associate, Session session) {
        return repository.existsVoteByAssociateAndSession(associate, session);
    }

}
