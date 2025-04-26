package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.helper.ExtractAuthenticatedUserUseCase;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.mapper.UserMapper;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAuthenticatedUserDtoUseCase implements UseCaseWithReturn<UserDto> {

    private final UserMapper userMapper;
    private final ExtractAuthenticatedUserUseCase extractAuthenticatedUserUseCase;

    @Override
    public UserDto execute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = extractAuthenticatedUserUseCase.execute(authentication);
        return userMapper.toDto(user);
    }

}
