package com.gucardev.wallet.infrastructure.config.cache;


import java.util.Arrays;
import java.util.List;

public class CacheNames {

    public static final String CACHE_CONFIGURATIONS = "configurations";
    public static final String CACHE_PRODUCTS = "products";
    public static final String CACHE_USER_PREFERENCES = "user_preferences";
    public static final String CACHE_USER_SESSIONS = "user_sessions";
    public static final String CACHE_TEMPORARY_TOKENS = "temporary_tokens";

    public static List<String> getAllCacheNames() {
        return Arrays.asList(
                CACHE_CONFIGURATIONS,
                CACHE_PRODUCTS,
                CACHE_USER_PREFERENCES,
                CACHE_USER_SESSIONS,
                CACHE_TEMPORARY_TOKENS);
    }
}
