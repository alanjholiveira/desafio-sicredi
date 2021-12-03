package br.com.sicredi.desafio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PollNotFoundException extends RuntimeException {

    public PollNotFoundException() {
        super("Poll Not Found");
    }

}
