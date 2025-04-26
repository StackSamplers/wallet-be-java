package com.gucardev.wallet.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionMessage {
    DEFAULT_EXCEPTION("messages.error.default_message", HttpStatus.BAD_REQUEST, 1000),
    ALREADY_EXISTS_EXCEPTION("messages.error.already_exists_exception", HttpStatus.CONFLICT, 1001),
    NOT_FOUND_EXCEPTION("messages.error.not_found_exception", HttpStatus.NOT_FOUND, 1002),
    USER_NOT_FOUND_EXCEPTION("messages.error.user_not_found_exception", HttpStatus.NOT_FOUND, 1003),
    ACCESS_DENIED_EXCEPTION("messages.error.access_denied_exception", HttpStatus.UNAUTHORIZED, 1004),
    INVALID_OTP_EXCEPTION("messages.error.default_message", HttpStatus.NOT_FOUND, 1005),
    ACCOUNT_LOCKED("messages.error.access_denied_exception", HttpStatus.LOCKED, 1006),
    FORBIDDEN_EXCEPTION("messages.error.forbidden_exception", HttpStatus.FORBIDDEN, 1007),
    AUTHENTICATION_FAILED("messages.error.authentication_failed", HttpStatus.UNAUTHORIZED, 1008),
    TOO_MANY_REQUESTS_EXCEPTION("messages.error.too_many_requests", HttpStatus.TOO_MANY_REQUESTS, 1009),
    INSUFFICIENT_FUNDS_EXCEPTION("messages.error.insufficient_fund", HttpStatus.UNAUTHORIZED, 1010);

    private final String key;
    private final HttpStatus status;
    private final int businessErrorCode;

    ExceptionMessage(String key, HttpStatus httpStatus, int businessErrorCode) {
        this.key = key;
        this.status = httpStatus;
        this.businessErrorCode = businessErrorCode;
    }
}