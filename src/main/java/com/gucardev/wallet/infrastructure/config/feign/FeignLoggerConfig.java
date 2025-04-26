package com.gucardev.wallet.infrastructure.config.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignLoggerConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        // Options: NONE, BASIC, HEADERS, FULL
        return Logger.Level.FULL;
    }
}