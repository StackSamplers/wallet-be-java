package com.gucardev.wallet.infrastructure.config.cache.redis;

import com.gucardev.wallet.infrastructure.config.cache.CacheNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;

//@Cacheable(value = CacheNames.CACHE_PRODUCTS, key = "#id", cacheManager = RedisCacheConfig.REDIS_CACHE_MANAGER_MEDIUM_LIVED)

@Configuration
public class RedisCacheConfig {

    // Cache Manager Bean Names
    public static final String REDIS_CACHE_MANAGER_SHORT_LIVED = "redisShortLivedCacheManager";
    public static final String REDIS_CACHE_MANAGER_MEDIUM_LIVED = "redisMediumLivedCacheManager";

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);

        if (!redisPassword.isEmpty()) {
            redisConfig.setPassword(redisPassword);
        }

//        return new LettuceConnectionFactory(redisConfig);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);

        // This enables keyspace notifications for expired events
        factory.setShareNativeConnection(false);
        factory.afterPropertiesSet();

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean(REDIS_CACHE_MANAGER_SHORT_LIVED)
    public CacheManager redisShortLivedCacheManager(
            RedisConnectionFactory connectionFactory,
            @Value("${app-specific-configs.cache.short.ttl:60}") int shortTtlSeconds
    ) {
        return buildRedisCacheManager(connectionFactory, shortTtlSeconds);
    }

    @Bean(REDIS_CACHE_MANAGER_MEDIUM_LIVED)
    public CacheManager redisMediumLivedCacheManager(
            RedisConnectionFactory connectionFactory,
            @Value("${app-specific-configs.cache.medium.ttl:120}") int mediumTtlSeconds
    ) {
        return buildRedisCacheManager(connectionFactory, mediumTtlSeconds);
    }

    private CacheManager buildRedisCacheManager(RedisConnectionFactory connectionFactory, int ttlSeconds) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(ttlSeconds))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()
                        )
                );

        // Build cache manager with predefined cache names
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .initialCacheNames(new HashSet<>(CacheNames.getAllCacheNames()))
                .build();
    }
}