package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found")
public class RecordNotFoundException extends Exception {

    public RecordNotFoundException(String message) {
        this(message, null);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
