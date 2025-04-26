package com.gucardev.wallet.infrastructure.exception.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final boolean logStackTrace;
    private final Integer businessErrorCode;

    public CustomException(String message, HttpStatus status, Integer businessErrorCode) {
        super(message);
        this.status = status;
        this.logStackTrace = true;
        this.businessErrorCode = businessErrorCode;
    }

    public CustomException(String message, HttpStatus status, Integer businessErrorCode, boolean logStackTrace) {
        super(message);
        this.status = status;
        this.logStackTrace = logStackTrace;
        this.businessErrorCode = businessErrorCode;
    }

}