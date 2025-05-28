package com.gucardev.wallet.domain.auth.usecase.otp.confirm.email;

import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
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
import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;

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
        params.setSendingChannel(OtpSendingChannel.EMAIL);

        var user = getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildSilentException(ExceptionMessage.NOT_FOUND_EXCEPTION));

        if (!validateOtpUseCase.execute(params)) {
            throw buildSilentException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }

        user.setActivated(true);
        userRepository.save(user);
    }
}
