package com.gucardev.wallet.domain.auth.usecase.refreshtoken;

import com.gucardev.wallet.domain.auth.entity.RefreshToken;
import com.gucardev.wallet.domain.auth.repository.RefreshTokenRepository;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app-specific-configs.security.jwt.refresh-token-validity-in-minutes}")
    private Integer refreshTokenExpiresInMinutes;

    @Transactional
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
                .orElseThrow(() -> buildSilentException(ExceptionMessage.NOT_FOUND_EXCEPTION, token));
    }

    public boolean isTokenValid(RefreshToken token) {
        return token.getExpiryTime().isAfter(LocalDateTime.now());
    }
}