package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.entity.RefreshToken;
import com.gucardev.wallet.domain.auth.model.dto.RefreshTokenRequest;
import com.gucardev.wallet.domain.auth.model.dto.TokenDto;
import com.gucardev.wallet.domain.auth.usecase.refreshtoken.RefreshTokenService;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateTokenByRefreshTokenUseCase implements UseCaseWithParamsAndReturn<RefreshTokenRequest, TokenDto> {

    private final RefreshTokenService refreshTokenService;
    private final GenerateTokenTokenUseCase generateTokenTokenUseCase;

    @Override
    public TokenDto execute(RefreshTokenRequest params) {
        RefreshToken refreshToken = refreshTokenService.findByToken(params.getToken());
        if (!refreshTokenService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token is expired or invalid!");
        }
        return generateTokenTokenUseCase.execute(refreshToken.getUser());
    }
}
