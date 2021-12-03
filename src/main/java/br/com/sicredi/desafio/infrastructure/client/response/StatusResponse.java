package br.com.sicredi.desafio.infrastructure.client.response;

import br.com.sicredi.desafio.infrastructure.enums.AssociateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {

    private AssociateStatus status;

}
