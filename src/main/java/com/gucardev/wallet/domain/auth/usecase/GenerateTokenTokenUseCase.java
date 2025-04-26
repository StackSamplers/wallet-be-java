package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.model.dto.TokenDto;
import com.gucardev.wallet.domain.auth.usecase.refreshtoken.RefreshTokenService;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.mapper.UserMapper;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import com.gucardev.wallet.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateTokenTokenUseCase implements UseCaseWithParamsAndReturn<User, TokenDto> {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Override
    public TokenDto execute(User params) {
        // Generate JWT token with user roles
        String tokenString = jwtUtil.generateToken(
                params.getEmail(),
                Map.of("roles", params.getRoles())
        );
        // Generate Refresh Token
        var refreshToken = refreshTokenService.generateAndSaveRefreshToken(params);

        // Build and return response
        return TokenDto.builder()
                .accessToken(tokenString)
                .refreshToken(refreshToken)
                .user(userMapper.toDto(params))
                .build();
    }
}
