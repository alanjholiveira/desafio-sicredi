package br.com.sicredi.desafio.domain.service;

import br.com.sicredi.desafio.builder.entity.AssociateBuilder;
import br.com.sicredi.desafio.domain.entity.Associate;
import br.com.sicredi.desafio.infrastructure.client.AssociateStatusClient;
import br.com.sicredi.desafio.infrastructure.client.response.StatusResponse;
import br.com.sicredi.desafio.infrastructure.enums.AssociateStatus;
import br.com.sicredi.desafio.infrastructure.repository.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AssociateServiceTest {

    @InjectMocks
    private AssociateService service;

    @Mock
    private AssociateRepository repository;

    @Mock
    private AssociateStatusClient statusClient;

    @Autowired
    private AssociateBuilder associateBuilder;

    @Test
    void when_getAll_returns_success() throws ParseException {
        Associate associateBuild = associateBuilder.construirEntidade();
        when(repository.findAll()).thenReturn(List.of(associateBuild));

        List<Associate> associate = service.findAll();

        assertNotNull(associate);
        assertEquals(List.of(associateBuild), associate);
    }

    @Test
    void when_save_return_success() throws ParseException {
        Associate associateBuild = associateBuilder.construirEntidade();
        when(repository.save(associateBuild)).thenReturn(associateBuild);
        when(statusClient.getStatus(associateBuild.getTaxId()))
                .thenReturn(StatusResponse.builder().status(AssociateStatus.ABLE_TO_VOTE).build());

        Associate associate = service.save(associateBuild);

        assertNotNull(associate);
        assertEquals(associateBuild, associate);
    }

}
