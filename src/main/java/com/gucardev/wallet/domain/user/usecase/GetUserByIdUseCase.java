package com.gucardev.wallet.domain.user.usecase;

import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.repository.UserRepository;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase implements UseCaseWithParamsAndReturn<Long, Optional<User>> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> execute(Long id) {
        return userRepository.findById(id);
    }

    public User executeAndThrowExceptionIfNotFound(Long id) {
        var result = execute(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw buildException(ExceptionMessage.NOT_FOUND_EXCEPTION, id);
    }

}
