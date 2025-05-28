package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import com.gucardev.wallet.domain.auth.model.request.GenerateEmailOtpRequest;
import com.gucardev.wallet.domain.auth.model.request.UserRegisterRequest;
import com.gucardev.wallet.domain.auth.usecase.otp.SendOtpForRegisterUseCase;
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
    private final SendOtpForRegisterUseCase sendOtpForRegisterUseCase;

    @Override
    public void execute(UserRegisterRequest params) {
        var createdUser = createUserUseCase.execute(new UserCreateRequest(params));
        sendOtpForRegisterUseCase.execute(new GenerateEmailOtpRequest(createdUser.getEmail(), OtpType.REGISTER_EMAIL_VERIFICATION));
    }

}
