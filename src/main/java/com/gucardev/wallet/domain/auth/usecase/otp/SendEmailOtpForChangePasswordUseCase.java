package com.gucardev.wallet.domain.auth.usecase.otp;

import com.gucardev.wallet.domain.otp.model.response.OtpResponse;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingType;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.usecase.GenerateOtpUseCase;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailOtpForChangePasswordUseCase implements UseCaseWithParamsAndReturn<OtpSendingRequest, OtpResponse> {

    private final GenerateOtpUseCase generateOtpUseCase;

    @Override
    public OtpResponse execute(OtpSendingRequest params) {
        params.setType(OtpType.CHANGE_PASSWORD);
        var generatedOtp = generateOtpUseCase.execute(params);
        if (generatedOtp.isSent()) {
            return generatedOtp;
        }
        // todo sent otp sms/email here
        OtpSendingType.fromType(OtpSendingType.EMAIL).getService().sendNotification(params);
        return generatedOtp;
    }
}
