package com.gucardev.wallet.infrastructure.exception;

import com.gucardev.wallet.infrastructure.config.message.MessageUtil;
import com.gucardev.wallet.infrastructure.exception.helper.BaseExceptionHandler;
import com.gucardev.wallet.infrastructure.exception.model.ClientRequestException;
import com.gucardev.wallet.infrastructure.exception.model.BusinessException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Hidden // for openapi doc
@RestControllerAdvice
public class GlobalBaseExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleCustomException(BusinessException exception, WebRequest request) {
        log.warn("Custom exception occurred: {}", exception.getLocalizedMessage());
        return this.buildErrorResponse(exception.getMessage(), exception.getStatus(), request, exception.getBusinessErrorCode());
    }

    @ExceptionHandler(ClientRequestException.class)
    public ResponseEntity<Object> clientRequestException(ClientRequestException exception, WebRequest request) {
        if (exception.isLogStackTrace()) {
            log.error("Client request exception occurred: ", exception);
        } else {
            log.error("Client request exception occurred: {}", exception.getMessage());
        }
        // Assuming ClientRequestException also has businessErrorCode, adjust accordingly if not
        return this.buildErrorResponse(exception.getMessage(), exception.getStatus(), request, exception.getBusinessErrorCode());
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<Object> noResourceFoundException(NoResourceFoundException exception, WebRequest request) {
        log.warn("Resource not found: {}", exception.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        return this.buildErrorResponse(exception.getMessage(), status, request, ExceptionMessage.NOT_FOUND_EXCEPTION.getBusinessErrorCode());
    }

    @ExceptionHandler({RequestNotPermitted.class})
    public ResponseEntity<Object> requestNotPermittedException(RequestNotPermitted exception, WebRequest request) {
        log.warn("request not permitted, too many requests: {}", exception.getMessage());
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        return this.buildErrorResponse(exception.getMessage(), status, request, ExceptionMessage.TOO_MANY_REQUESTS_EXCEPTION.getBusinessErrorCode());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<Object> handleMethodArgumentNotValidEx(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = extractFieldErrors(ex);
        log.warn("Validation failed: {}", errors);
        return this.buildErrorResponse(
                MessageUtil.getMessage("validation.failed"),
                HttpStatus.BAD_REQUEST,
                request,
                errors,
                ExceptionMessage.DEFAULT_EXCEPTION.getBusinessErrorCode()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        return this.buildErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<Object> handleConstraintViolationEx(
            ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = extractConstraintViolations(ex);
        log.warn("Constraint violation: {}", errors);
        return this.buildErrorResponse(
                MessageUtil.getMessage("validation.failed"),
                HttpStatus.BAD_REQUEST,
                request,
                errors,
                ExceptionMessage.DEFAULT_EXCEPTION.getBusinessErrorCode()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        String errorMessage = MessageUtil.getMessage("error.request.body.missing");
        log.warn("Message not readable: {}", ex.getMessage());
        return this.buildErrorResponse(
                errorMessage,
                HttpStatus.BAD_REQUEST,
                request,
                ExceptionMessage.DEFAULT_EXCEPTION.getBusinessErrorCode()
        );
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        String message;
        int errorCode;
        if (isDatabaseException(ex)) {
            log.error("Database exception occurred: ", ex);
            message = "database.error";
        } else {
            log.error("Exception occurred: ", ex);
//            message = "default.error";
            message = ex.getLocalizedMessage();
            errorCode = ExceptionMessage.UNEXPECTED_EXCEPTION.getBusinessErrorCode();
            return this.buildErrorResponse(message, HttpStatus.BAD_REQUEST, request, errorCode);
        }
        errorCode = ExceptionMessage.DEFAULT_EXCEPTION.getBusinessErrorCode();
        return this.buildErrorResponse(MessageUtil.getMessage(message), HttpStatus.BAD_REQUEST, request, errorCode);
    }

    private Map<String, String> extractFieldErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private Map<String, String> extractConstraintViolations(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private boolean isDatabaseException(Throwable ex) {
        return ex instanceof SQLException ||
                ex instanceof DataAccessException ||
                ex instanceof PersistenceException ||
                (ex.getCause() != null && isDatabaseException(ex.getCause()));
    }
}
