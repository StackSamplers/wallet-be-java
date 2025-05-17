package com.gucardev.wallet.infrastructure.config.cache.redis.persist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

/**
 * Service for storing UUIDs as keys in Redis with expiration
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UuidCacheService {

    @Qualifier("uuidRedisTemplate")
    private final RedisTemplate<String, String> uuidRedisTemplate;

    // Prefix to identify your UUID entries in Redis
    private static final String PREFIX = "uuid:";

    // Constant value to use for all entries
    private static final String PLACEHOLDER_VALUE = "1";

    public boolean storeUuid(String uuid, long ttlSeconds) {
        String key = PREFIX + uuid;
        try {
            uuidRedisTemplate.opsForValue().set(key, PLACEHOLDER_VALUE, Duration.ofSeconds(ttlSeconds));
            log.info("Stored UUID key: {} with TTL: {} seconds", key, ttlSeconds);
            return true;
        } catch (Exception e) {
            log.error("Error storing UUID key: {}", key, e);
            return false;
        }
    }

    public boolean existsUuid(String uuid) {
        String key = PREFIX + uuid;
        try {
            return Boolean.TRUE.equals(uuidRedisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking UUID key: {}", key, e);
            return false;
        }
    }

    public boolean deleteUuid(String uuid) {
        String key = PREFIX + uuid;
        try {
            return Boolean.TRUE.equals(uuidRedisTemplate.delete(key));
        } catch (Exception e) {
            log.error("Error deleting UUID key: {}", key, e);
            return false;
        }
    }

    public Set<String> getAllUuids() {
        try {
            Set<String> keys = uuidRedisTemplate.keys(PREFIX + "*");
            // Remove the prefix to get just the UUIDs
            return keys.stream()
                    .map(key -> key.substring(PREFIX.length()))
                    .collect(java.util.stream.Collectors.toSet());
        } catch (Exception e) {
            log.error("Error finding UUID keys", e);
            return Set.of();
        }
    }

    public long getUuidTimeToLive(String uuid) {
        String key = PREFIX + uuid;
        try {
            return uuidRedisTemplate.getExpire(key);
        } catch (Exception e) {
            log.error("Error getting TTL for UUID key: {}", key, e);
            return -2;
        }
    }

    public boolean updateUuidTTL(String uuid, long ttlSeconds) {
        String key = PREFIX + uuid;
        try {
            return Boolean.TRUE.equals(
                    uuidRedisTemplate.expire(key, Duration.ofSeconds(ttlSeconds))
            );
        } catch (Exception e) {
            log.error("Error updating TTL for UUID key: {}", key, e);
            return false;
        }
    }

    public String extractUuidFromKey(String redisKey) {
        if (redisKey != null && redisKey.startsWith(PREFIX)) {
            return redisKey.substring(PREFIX.length());
        }
        return redisKey; // Return as-is if not matching the prefix pattern
    }
}