package br.com.sicredi.desafio.domain.entity;

import br.com.sicredi.desafio.infrastructure.enums.VoteType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_vote")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    private Associate associate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    @CreatedDate
    private LocalDateTime createdAt;

}
