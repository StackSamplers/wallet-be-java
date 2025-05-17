package com.gucardev.wallet.infrastructure.config.cache.redis;


import com.gucardev.wallet.infrastructure.config.cache.redis.persist.UuidCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyListener implements MessageListener {

    private final UuidCacheService uuidCacheService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("expired key {}", expiredKey);
        if (expiredKey.startsWith("uuid:")) {
            // Extract the UUID portion
            String uuid = uuidCacheService.extractUuidFromKey(expiredKey);
            log.info("UUID expired: {}", uuid);
        }
    }
}