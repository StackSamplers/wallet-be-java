package com.gucardev.wallet.domain.user.usecase;

import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.repository.UserRepository;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase implements UseCaseWithParamsAndReturn<Long, Optional<User>> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> execute(Long id) {
        return userRepository.findById(id);
    }

}
