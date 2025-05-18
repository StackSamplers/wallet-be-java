package com.gucardev.wallet.domain.auth.usecase.otp;

import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import com.gucardev.wallet.domain.auth.model.response.OtpResponse;
import com.gucardev.wallet.domain.auth.model.request.GenerateOtpRequest;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOtpForChangePasswordUseCase implements UseCaseWithParamsAndReturn<GenerateOtpRequest, OtpResponse> {

    private final GenerateOtpUseCase generateOtpUseCase;

    @Override
    public OtpResponse execute(GenerateOtpRequest params) {
        params.setType(OtpType.CHANGE_PASSWORD);
        var generatedOtp = generateOtpUseCase.execute(params);
        if (generatedOtp.isSent()) {
            return generatedOtp;
        }
        // todo sent otp sms/email here
        return generatedOtp;
    }
}
