package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.helper.ExtractAuthenticatedUserUseCase;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class GetAuthenticatedUserUseCase implements UseCaseWithReturn<User> {

    private final ExtractAuthenticatedUserUseCase extractAuthenticatedUserUseCase;

    @Override
    public User execute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return extractAuthenticatedUserUseCase.execute(authentication);
    }

}