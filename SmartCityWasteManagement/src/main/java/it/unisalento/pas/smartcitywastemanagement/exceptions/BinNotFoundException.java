package it.unisalento.pas.smartcitywastemanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BinNotFoundException extends RuntimeException {

    public BinNotFoundException(String binId) {
        super("Il bin con ID " + binId + " non è stato trovato.");
    }
}
