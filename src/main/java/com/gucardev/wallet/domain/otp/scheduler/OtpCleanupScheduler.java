package com.gucardev.wallet.domain.otp.scheduler;

import com.gucardev.wallet.domain.otp.repository.UserOtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtpCleanupScheduler {

    private final UserOtpRepository userOtpRepository;

    // Run every minute to clean up expired OTPs
    @Scheduled(fixedRate = 60000) // 60 seconds = 1 minute
    @Transactional
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        userOtpRepository.deleteExpiredOtpS(now);
        log.debug("Expired OTPs cleanup task executed at: {}", now);
    }
}