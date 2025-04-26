package com.gucardev.wallet.domain.auth.helper;

import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.infrastructure.config.security.userdetails.CustomUserDetails;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtractAuthenticatedUserUseCase implements UseCaseWithParamsAndReturn<Authentication, User> {

    @Override
    public User execute(Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new RuntimeException("No valid authentication found");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new RuntimeException("Principal is not of expected type");
        }

        return ((CustomUserDetails) principal).getUser();
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}

