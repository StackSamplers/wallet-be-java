package com.gucardev.wallet.domain.auth.usecase.otp;

import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import com.gucardev.wallet.domain.auth.model.response.OtpResponse;
import com.gucardev.wallet.domain.auth.model.request.GenerateEmailOtpRequest;
import com.gucardev.wallet.domain.notification.model.OtpEmailRequest;
import com.gucardev.wallet.domain.notification.usecase.SendOTPMailUseCase;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOtpForChangePasswordUseCase implements UseCaseWithParamsAndReturn<GenerateEmailOtpRequest, OtpResponse> {

    private final GenerateEmailOtpUseCase generateEmailOtpUseCase;
    private final SendOTPMailUseCase sendOTPMailUseCase;
    @Override
    public OtpResponse execute(GenerateEmailOtpRequest params) {
        params.setType(OtpType.CHANGE_PASSWORD);
        var generatedOtp = generateEmailOtpUseCase.execute(params);
        if (generatedOtp.isSent()) {
            return generatedOtp;
        }
        // todo sent otp sms/email here
        sendOTPMailUseCase.execute(new OtpEmailRequest(params.getEmail(), generatedOtp.getOtp()));
        return generatedOtp;
    }
}
