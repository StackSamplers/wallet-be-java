package com.gucardev.wallet.infrastructure.config.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.gucardev.wallet.infrastructure.config.cache.CacheNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CaffeineCacheConfig {

    // Cache Manager Bean Names
    public static final String CACHE_MANAGER_SHORT_LIVED = "shortLivedCacheManager";
    public static final String CACHE_MANAGER_MEDIUM_LIVED = "mediumLivedCacheManager";

    @Primary
    @Bean(CACHE_MANAGER_SHORT_LIVED)
    public CacheManager shortLivedCacheManager(
            @Value("${app-specific-configs.cache.short.ttl:60}") int shortTtlSeconds,
            @Value("${app-specific-configs.cache.default.max-size:1000}") int defaultMaxSize
    ) {
        return buildCacheManager(shortTtlSeconds, defaultMaxSize);
    }

    @Bean(CACHE_MANAGER_MEDIUM_LIVED)
    public CacheManager mediumLivedCacheManager(
            @Value("${app-specific-configs.cache.medium.ttl:120}") int mediumTtlSeconds,
            @Value("${app-specific-configs.cache.default.max-size:1000}") int defaultMaxSize
    ) {
        return buildCacheManager(mediumTtlSeconds, defaultMaxSize);
    }

    private CacheManager buildCacheManager(int ttlSeconds, int maxSize) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(CacheNames.getAllCacheNames());
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
                        .maximumSize(maxSize)
        );
        return cacheManager;
    }
}