package com.gucardev.wallet.infrastructure.config.feign;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignBasicAuthConfig {

    @Value("${app-specific-configs.api.auth.username}")
    private String username;

    @Value("${app-specific-configs.api.auth.password}")
    private String password;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
