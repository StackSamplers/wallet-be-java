package com.gucardev.wallet.domain.otp.usecase;

import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.otp.entity.UserOtp;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.repository.UserOtpRepository;
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
        String destination = params.getDestination();
        OtpType type = params.getType();
        OtpSendingChannel sendingChannel = params.getSendingChannel();
        String otp = params.getOtp();

        // Find OTP for the provided destination
        UserOtp userOtp = userOtpRepository.findByDestinationAndTypeAndSendingChannel(destination, type, sendingChannel)
                .orElseThrow(() -> buildSilentException(ExceptionMessage.INVALID_OTP_EXCEPTION));

        // Check if OTP is expired
        if (userOtp.isExpired()) {
            userOtpRepository.delete(userOtp);
            log.warn("OTP expired for destination: {}, type: {}", destination, type);
            return false;
        }

        // Validate OTP
        if (!userOtp.getOtp().equals(otp)) {
            log.warn("Invalid OTP provided for destination: {}, type: {}", destination, type);
            return false;
        }

        // OTP is valid, delete it after successful validation
        userOtpRepository.delete(userOtp);
        log.info("OTP validated successfully for destination: {}, type: {}", destination, type);

        return true;
    }
}