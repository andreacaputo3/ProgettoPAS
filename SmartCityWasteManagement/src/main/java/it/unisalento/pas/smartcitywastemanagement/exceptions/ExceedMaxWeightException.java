package it.unisalento.pas.smartcitywastemanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ExceedMaxWeightException extends RuntimeException {

    public ExceedMaxWeightException(String binLocation) {
        super("Il peso del disposal supera la capacità massima del bin situato in " + binLocation + ".");
    }
}
