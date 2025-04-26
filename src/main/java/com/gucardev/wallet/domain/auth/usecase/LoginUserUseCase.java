package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.helper.ExtractAuthenticatedUserUseCase;
import com.gucardev.wallet.domain.auth.model.dto.LoginRequest;
import com.gucardev.wallet.domain.auth.model.dto.TokenDto;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.mapper.UserMapper;
import com.gucardev.wallet.domain.user.usecase.GetUserByEmailUseCase;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import com.gucardev.wallet.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.gucardev.wallet.infrastructure.exception.ExceptionMessage.ACCESS_DENIED_EXCEPTION;
import static com.gucardev.wallet.infrastructure.exception.ExceptionMessage.ACCOUNT_LOCKED;
import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserUseCase implements UseCaseWithParamsAndReturn<LoginRequest, TokenDto> {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final UserMapper userMapper;
    private final ExtractAuthenticatedUserUseCase extractAuthenticatedUserUseCase;

    @Override
    public TokenDto execute(LoginRequest params) {
        String email = params.getEmail();

        try {
            // Authenticate user
            Authentication authentication = authenticateUser(params);

            // Extract user details
            User user = extractAuthenticatedUserUseCase.execute(authentication);

            // Generate JWT token with user roles
            String tokenString = jwtUtil.generateToken(
                    user.getEmail(),
                    Map.of("roles", user.getRoles())
            );

            // Build and return response
            return TokenDto.builder()
                    .accessToken(tokenString)
                    .user(userMapper.toDto(user))
                    .build();

        } catch (Exception ex) {
            handleLoginFailure(email, ex);
            return null; // This line is never reached due to exception throwing
        }
    }

    private void handleLoginFailure(String email, Exception ex) {
        // Check if user exists
        Optional<User> userOptional = getUserByEmailUseCase.execute(email);

        if (userOptional.isEmpty()) {
            log.warn("Authentication failed: User not found for email: {}", email);
            throw buildSilentException(ACCESS_DENIED_EXCEPTION);
        }

        User user = userOptional.get();

        // Check if account is activated
        if (!user.getActivated()) {
            log.warn("Authentication failed: Account not activated for email: {}", email);
            throw buildSilentException(ACCOUNT_LOCKED);
        }

        // General authentication failure
        log.warn("Authentication failed: Invalid credentials for email: {}", email);
        throw buildSilentException(ACCESS_DENIED_EXCEPTION);
    }

    private Authentication authenticateUser(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
    }
}
