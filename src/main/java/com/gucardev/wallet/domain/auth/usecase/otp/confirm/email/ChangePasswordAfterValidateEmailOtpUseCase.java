package com.gucardev.wallet.domain.auth.usecase.otp.confirm.email;

import com.gucardev.wallet.domain.auth.model.request.ChangePasswordConfirmRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordAfterValidateEmailOtpUseCase implements UseCaseWithParams<ChangePasswordConfirmRequest> {

    private final ValidateOtpUseCase validateOtpUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void execute(ChangePasswordConfirmRequest params) {
        String email = params.getEmail();
        var user = getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));
        if (!validateOtpUseCase.execute(new ValidateOtpRequest(params.getEmail(), OtpType.CHANGE_PASSWORD, OtpSendingType.EMAIL, params.getOtp()))) {
            throw buildException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }
        user.setPassword(bCryptPasswordEncoder.encode(params.getPassword()));
        userRepository.save(user);
    }
}
