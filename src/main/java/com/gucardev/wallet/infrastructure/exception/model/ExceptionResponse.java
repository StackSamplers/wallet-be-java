package com.gucardev.wallet.infrastructure.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "error",
        "traceId",
        "path",
        "status",
        "businessErrorCode",
        "message",
        "time",
        "hasValidationErrors",
        "validationErrors",
})
@ToString
public class ExceptionResponse {

    @Builder.Default
    private final boolean isError = true;
    private final HttpStatus status;
    private final Integer businessErrorCode;
    private final String traceId;
    private final Object message;
    private final String path;
    private final Map<String, String> validationErrors;
    private final boolean hasValidationErrors;

    @Builder.Default
    private final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public static ResponseEntity<ExceptionResponse> buildResponse(HttpStatus status, Object message,Integer businessErrorCode, String path, String traceId, Map<String, String> validationErrors) {
        return ResponseEntity.status(status).body(
                ExceptionResponse.builder()
                        .businessErrorCode(businessErrorCode)
                        .status(status)
                        .message(message)
                        .path(path)
                        .traceId(traceId)
                        .validationErrors(validationErrors)
                        .hasValidationErrors(validationErrors != null && !validationErrors.isEmpty())
                        .build()
        );
    }
}


