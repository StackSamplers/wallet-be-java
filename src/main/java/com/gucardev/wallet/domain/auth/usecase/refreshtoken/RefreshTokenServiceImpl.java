package com.gucardev.wallet.domain.auth.usecase.refreshtoken;

import com.gucardev.wallet.domain.auth.entity.RefreshToken;
import com.gucardev.wallet.domain.auth.repository.RefreshTokenRepository;
import com.gucardev.wallet.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app-specific-configs.security.jwt.refresh-token-validity-in-minutes}")
    private Integer refreshTokenExpiresInMinutes;

    public String generateAndSaveRefreshToken(User params) {
        // Delete old tokens for that user
        refreshTokenRepository.deleteByUser_Id(params.getId());
        // Create and save a new refresh token
        String tokenValue = UUID.randomUUID().toString();

        // Convert Instant to LocalDateTime properly
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(refreshTokenExpiresInMinutes);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .expiryTime(expiryDate)
                .user(params)
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenValue;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));
    }

    public boolean isTokenValid(RefreshToken token) {
        return token.getExpiryTime().isAfter(LocalDateTime.now());
    }

}
