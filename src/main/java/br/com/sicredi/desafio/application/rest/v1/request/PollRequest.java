package br.com.sicredi.desafio.application.rest.v1.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PollRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

}
