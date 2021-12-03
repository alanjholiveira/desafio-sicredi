package br.com.sicredi.desafio.builder.entity;

import br.com.sicredi.desafio.builder.ConstrutorDeEntidade;
import br.com.sicredi.desafio.domain.entity.Session;
import br.com.sicredi.desafio.infrastructure.enums.SessionStatus;
import br.com.sicredi.desafio.infrastructure.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Component
public class SessionBuilder extends ConstrutorDeEntidade<Session, UUID> {

    @Autowired
    private SessionRepository repository;

    @Autowired PollBuilder pollBuilder;

    @Override
    public Session construirEntidade() throws ParseException {
        return Session.builder()
                .id(UUID.randomUUID())
                .poll(pollBuilder.construirEntidade())
                .status(SessionStatus.OPEN)
                .expiration(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Override
    public Session persistir(Session entidade) {
        return repository.save(entidade);
    }

    @Override
    protected Collection<Session> obterTodos() {
        return repository.findAll();
    }

    @Override
    protected Session obterPorId(UUID id) {
        return repository.findById(id).get();
    }
}
