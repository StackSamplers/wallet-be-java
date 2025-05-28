package com.gucardev.wallet.domain.auth.usecase.otp.confirm.email;

import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingType;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.usecase.ValidateOtpUseCase;
import com.gucardev.wallet.domain.user.repository.UserRepository;
import com.gucardev.wallet.domain.user.usecase.GetUserByEmailUseCase;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivateUserByEmailOtpUseCase implements UseCaseWithParams<ValidateOtpRequest> {

    private final ValidateOtpUseCase validateOtpUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final UserRepository userRepository;

    @Override
    public void execute(ValidateOtpRequest params) {
        String email = params.getDestination();
        params.setType(OtpType.REGISTER_EMAIL_VERIFICATION);
        params.setSendingType(OtpSendingType.EMAIL);

        // Check if email exists in the system
        getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, email));

        if (!validateOtpUseCase.execute(params)) {
            throw buildException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }

        var user = getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));

        user.setActivated(true);
        userRepository.save(user);
    }
}
