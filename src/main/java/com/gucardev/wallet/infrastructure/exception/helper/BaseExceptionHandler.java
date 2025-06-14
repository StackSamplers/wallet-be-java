package com.gucardev.wallet.infrastructure.exception.helper;

import com.gucardev.wallet.infrastructure.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Slf4j
public abstract class BaseExceptionHandler {

    private static final String TRACE_ID_LOG_VAR_NAME = "traceId";

    protected final ResponseEntity<Object> buildErrorResponse(Object error, HttpStatus status, WebRequest request, Map<String, String> validationErrors) {
        return buildErrorResponse(error, status, request, validationErrors, null);
    }

    protected final ResponseEntity<Object> buildErrorResponse(Object error, HttpStatus status, WebRequest request) {
        return buildErrorResponse(error, status, request, null, null);
    }

    protected final ResponseEntity<Object> buildErrorResponse(Object error, HttpStatus status, WebRequest request, Integer businessErrorCode) {
        return buildErrorResponse(error, status, request, null, businessErrorCode);
    }

    protected final ResponseEntity<Object> buildErrorResponse(Object error, HttpStatus status, WebRequest request,
                                                              Map<String, String> validationErrors, Integer businessErrorCode) {
        String path = extractPath(request);
        String traceId = MDC.get(TRACE_ID_LOG_VAR_NAME);

        var builder = (validationErrors != null && !validationErrors.isEmpty())
                ? ApiResponse.validationError()
                : ApiResponse.error();

        var response = builder
                .message(error.toString())
                .status(status)
                .traceId(traceId)
                .path(path)
                .businessErrorCode(businessErrorCode)
                .validationErrors(validationErrors)
                .build();

        log.warn("Error response: status={}, message={}, traceId={}, path={}",
                status, error, traceId, path);

        return response;
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}