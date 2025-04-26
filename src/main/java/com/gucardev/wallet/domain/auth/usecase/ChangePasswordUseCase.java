package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import com.gucardev.wallet.domain.auth.model.request.ChangePasswordRequest;
import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.auth.usecase.otp.ValidateOtpUseCase;
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
public class ChangePasswordUseCase implements UseCaseWithParams<ChangePasswordRequest> {

    private final ValidateOtpUseCase validateOtpUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void execute(ChangePasswordRequest params) {
        String email = params.getEmail();
        if (!validateOtpUseCase.execute(new ValidateOtpRequest(params.getEmail(), OtpType.CHANGE_PASSWORD, params.getOtp()))) {
            throw buildException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }
        var user = getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));
        user.setPassword(bCryptPasswordEncoder.encode(params.getPassword()));
        userRepository.save(user);
    }
}
