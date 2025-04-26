package com.gucardev.wallet.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StartupConfig implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("Starting Wallet App Backend Spring Boot");
    }
}