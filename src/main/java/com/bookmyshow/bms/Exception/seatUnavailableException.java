package com.bookmyshow.bms.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class seatUnavailableException extends RuntimeException{
    public seatUnavailableException(String message) {
        super(message);
    }
}
