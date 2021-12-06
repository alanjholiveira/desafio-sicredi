package br.com.sicredi.desafio.application.rest.v1.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String taxId;

}
