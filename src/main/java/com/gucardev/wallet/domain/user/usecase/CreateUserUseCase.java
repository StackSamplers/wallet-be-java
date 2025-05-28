package com.gucardev.wallet.domain.user.usecase;

import com.gucardev.wallet.domain.user.mapper.UserMapper;
import com.gucardev.wallet.domain.user.model.request.UserCreateRequest;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import com.gucardev.wallet.domain.user.repository.UserRepository;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;


@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase implements UseCaseWithParamsAndReturn<UserCreateRequest, UserDto> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto execute(UserCreateRequest params) {

        if (userRepository.existsByEmailIgnoreCase(params.getEmail())) {
            throw buildSilentException(ExceptionMessage.ALREADY_EXISTS_EXCEPTION, params.getEmail());
        }
        var newUser = userMapper.toEntity(params);
        newUser.setActivated(false);
        newUser.setRoles(params.getRoles());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        var savedUser = userRepository.save(newUser);
        return userMapper.toDto(savedUser);
    }

}
