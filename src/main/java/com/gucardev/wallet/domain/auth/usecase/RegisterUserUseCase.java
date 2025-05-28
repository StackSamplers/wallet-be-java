package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.otp.model.request.RegisterOtpRequest;
import com.gucardev.wallet.domain.auth.model.request.UserRegisterRequest;
import com.gucardev.wallet.domain.auth.usecase.otp.SendEmailOtpForRegisterUseCase;
import com.gucardev.wallet.domain.user.model.request.UserCreateRequest;
import com.gucardev.wallet.domain.user.usecase.CreateUserUseCase;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserUseCase implements UseCaseWithParams<UserRegisterRequest> {

    private final CreateUserUseCase createUserUseCase;
    private final SendEmailOtpForRegisterUseCase sendEmailOtpForRegisterUseCase;

    @Override
    public void execute(UserRegisterRequest params) {
        var createdUser = createUserUseCase.execute(new UserCreateRequest(params));
        sendEmailOtpForRegisterUseCase.execute(new RegisterOtpRequest(createdUser.getEmail()));
    }

}
