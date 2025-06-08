package com.gucardev.wallet.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gucardev.wallet.infrastructure.config.message.MessageUtil;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@JsonPropertyOrder({
        "time",
        "error",
        "status",
        "message",
        "data",
        "traceId",
        "path",
        "businessErrorCode",
        "hasValidationErrors",
        "validationErrors",
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Common fields
    private String time;
    private Boolean error;
    private String status;
    private String message;

    // Success response field
    private T data;

    // Error response fields
    private String traceId;
    private String path;
    private Integer businessErrorCode;
    private Boolean hasValidationErrors;
    private Map<String, String> validationErrors;

    // Builder pattern entry points
    public static <T> ApiResponseBuilder<T> success() {
        return new ApiResponseBuilder<T>().success();
    }

    public static <T> ApiResponseBuilder<T> success(String messageKey) {
        return new ApiResponseBuilder<T>().success().messageKey(messageKey);
    }

    public static <T> ApiResponseBuilder<T> successMessage(String message) {
        return new ApiResponseBuilder<T>().success().message(message);
    }

    public static <T> ApiResponseBuilder<T> error() {
        return new ApiResponseBuilder<T>().error();
    }

    public static <T> ApiResponseBuilder<T> error(String messageKey) {
        return new ApiResponseBuilder<T>().error().messageKey(messageKey);
    }

    public static <T> ApiResponseBuilder<T> errorMessage(String message) {
        return new ApiResponseBuilder<T>().error().message(message);
    }

    public static <T> ApiResponseBuilder<T> validationError() {
        return new ApiResponseBuilder<T>().validationError();
    }

    public static <T> ApiResponseBuilder<T> validationError(String messageKey) {
        return new ApiResponseBuilder<T>().validationError().messageKey(messageKey);
    }

    public static <T> ApiResponseBuilder<T> validationErrorMessage(String message) {
        return new ApiResponseBuilder<T>().validationError().message(message);
    }

    // Fluent Builder Class
    public static class ApiResponseBuilder<T> {
        private Boolean error;
        private HttpStatus httpStatus = HttpStatus.OK;
        private String message;
        private String messageKey;
        private T data;
        private String traceId;
        private String path;
        private Integer businessErrorCode;
        private Boolean hasValidationErrors = false;
        private Map<String, String> validationErrors;

        private ApiResponseBuilder() {
        }

        public ApiResponseBuilder<T> success() {
            this.error = false;
            this.httpStatus = HttpStatus.OK;
            this.hasValidationErrors = false;
            return this;
        }

        public ApiResponseBuilder<T> error() {
            this.error = true;
            this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            this.hasValidationErrors = false;
            return this;
        }

        public ApiResponseBuilder<T> validationError() {
            this.error = true;
            this.httpStatus = HttpStatus.BAD_REQUEST;
            this.hasValidationErrors = true;
            return this;
        }

        // Change return type to allow different data type
        public <D> ApiResponseBuilder<D> body(D data) {
            @SuppressWarnings("unchecked")
            ApiResponseBuilder<D> builder = (ApiResponseBuilder<D>) this;
            builder.data = data;
            return builder;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            this.messageKey = null; // Clear message key if direct message is set
            return this;
        }

        public ApiResponseBuilder<T> messageKey(String messageKey) {
            this.messageKey = messageKey;
            this.message = null; // Clear direct message if message key is set
            return this;
        }

        public ApiResponseBuilder<T> status(HttpStatus status) {
            this.httpStatus = status;
            return this;
        }

        public ApiResponseBuilder<T> status(int statusCode) {
            this.httpStatus = HttpStatus.valueOf(statusCode);
            return this;
        }

        public ApiResponseBuilder<T> traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public ApiResponseBuilder<T> path(String path) {
            this.path = path;
            return this;
        }

        public ApiResponseBuilder<T> businessErrorCode(Integer businessErrorCode) {
            this.businessErrorCode = businessErrorCode;
            return this;
        }

        public ApiResponseBuilder<T> validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            this.hasValidationErrors = validationErrors != null && !validationErrors.isEmpty();
            return this;
        }

        // Modified build method to always return ApiResponse wrapper
        public <R> ResponseEntity<R> build() {
            // Determine final message
            String finalMessage = this.message;
            if (finalMessage == null && this.messageKey != null) {
                finalMessage = MessageUtil.getMessage(this.messageKey);
            }
            if (finalMessage == null) {
                finalMessage = getDefaultMessage();
            }

            // Always return the ApiResponse wrapper for consistency
            ApiResponse<T> apiResponse = new ApiResponse<>();
            apiResponse.setError(this.error);
            apiResponse.setStatus(this.httpStatus.name());
            apiResponse.setMessage(finalMessage);
            apiResponse.setData(this.error ? null : this.data);
            apiResponse.setTraceId(this.error ? this.traceId : null);
            apiResponse.setPath(this.error ? this.path : null);
            apiResponse.setBusinessErrorCode(this.error ? this.businessErrorCode : null);
            apiResponse.setHasValidationErrors(this.error ? this.hasValidationErrors : null);
            apiResponse.setValidationErrors(this.error && this.hasValidationErrors ? this.validationErrors : null);
            apiResponse.setTime(LocalDateTime.now().format(TIME_FORMATTER));

            @SuppressWarnings("unchecked")
            R responseBody = (R) apiResponse;
            return ResponseEntity.status(this.httpStatus).body(responseBody);
        }

        private String getDefaultMessage() {
            if (Boolean.TRUE.equals(this.error)) {
                if (Boolean.TRUE.equals(this.hasValidationErrors)) {
                    return "Validation failed";
                }
                return "An error occurred";
            }
            return "Operation completed successfully";
        }
    }
}