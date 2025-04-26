package com.gucardev.wallet.infrastructure.config.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static String DEFAULT_AUDITOR;
    public static String API_BASE_URL;
    public static String API_KEY;

    @Value("${app-specific-configs.constants.default-auditor}")
    private String defaultAuditor;

    @Value("${app-specific-configs.constants.api-url}")
    private String apiUrlProperty;

    @Value("${app-specific-configs.constants.api-key}")
    private String apiKeyProperty;

    @PostConstruct
    public void init() {
        DEFAULT_AUDITOR = defaultAuditor;
        API_BASE_URL = apiUrlProperty;
        API_KEY = apiKeyProperty;
    }
}