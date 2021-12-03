package br.com.sicredi.desafio.infrastructure.repository;

import br.com.sicredi.desafio.domain.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
}
