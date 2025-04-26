package com.gucardev.wallet.domain.auth.usecase.otp;

import com.gucardev.wallet.domain.auth.entity.UserOtp;
import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.auth.repository.UserOtpRepository;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;


@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateOtpUseCase implements UseCaseWithParamsAndReturn<ValidateOtpRequest, Boolean> {

    private final UserOtpRepository userOtpRepository;

    @Override
    @Transactional
    public Boolean execute(ValidateOtpRequest params) {
        String email = params.getEmail();
        OtpType type = params.getType();
        String otp = params.getOtp();

        // Find OTP for the provided email
        UserOtp userOtp = userOtpRepository.findByEmailAndType(email, type)
                .orElseThrow(() -> buildSilentException(ExceptionMessage.INVALID_OTP_EXCEPTION));

        // Check if OTP is expired
        if (userOtp.isExpired()) {
            userOtpRepository.delete(userOtp);
            return false;
        }

        // Validate OTP
        if (!userOtp.getOtp().equals(otp)) {
            return false;
        }

        // OTP is valid, delete it after successful validation
        userOtpRepository.delete(userOtp);
        log.info("OTP validated successfully for email: {}", email);

        return true;
    }
}