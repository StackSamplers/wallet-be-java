package com.gucardev.wallet.domain.auth.scheduler;

import com.gucardev.wallet.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    // Run every minute to clean up expired refresh tokens
    @Scheduled(fixedRate = 60000) // 60 seconds = 1 minute
    @Transactional
    public void cleanupExpiredRefreshTokens() {
        LocalDateTime now = LocalDateTime.now();
        refreshTokenRepository.deleteExpiredRefreshTokens(now);
        log.debug("Expired Refresh tokens cleanup task executed at: {}", now);
    }

}