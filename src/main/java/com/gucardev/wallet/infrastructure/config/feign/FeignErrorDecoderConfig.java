package com.gucardev.wallet.infrastructure.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDeniedException;

import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;

public class FeignErrorDecoderConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 400) {
                return new BadRequestException("Bad request");
            } else if (response.status() == 401) {
                return new AuthenticationException("Unauthorized access");
            } else if (response.status() == 403) {
                return new AuthorizationDeniedException("Unauthorized access");
            } else if (response.status() == 404) {
                return new ResourceNotFoundException("Resource not found");
            } else if (response.status() >= 500) {
                return new ServiceUnavailableException("Service unavailable");
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
