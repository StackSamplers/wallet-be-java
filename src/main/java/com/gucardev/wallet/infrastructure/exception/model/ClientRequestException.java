package com.gucardev.wallet.infrastructure.exception.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ClientRequestException extends RuntimeException {
    private final HttpStatus status;
    private final boolean logStackTrace;
    private final Integer businessErrorCode;

    public ClientRequestException(String message, HttpStatus status, boolean logStackTrace, Integer businessErrorCode) {
        super(message);
        this.status = status;
        this.logStackTrace = logStackTrace;
        this.businessErrorCode = businessErrorCode;
    }
}