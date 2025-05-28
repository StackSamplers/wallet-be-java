package com.gucardev.wallet.domain.auth.usecase.otp.send.email;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingType;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.model.request.RegisterOtpRequest;
import com.gucardev.wallet.domain.otp.model.response.OtpResponse;
import com.gucardev.wallet.domain.otp.usecase.GenerateOtpUseCase;
import com.gucardev.wallet.domain.user.usecase.GetUserByEmailUseCase;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailOtpForRegisterUseCase implements UseCaseWithParamsAndReturn<RegisterOtpRequest, OtpResponse> {

    private final GenerateOtpUseCase generateOtpUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;

    @Override
    public OtpResponse execute(RegisterOtpRequest params) {
        String email = params.getDestination();

        // Check if email exists in the system
        getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, email));

        OtpSendingRequest otpSendingRequest = OtpSendingRequest.builder()
                .type(OtpType.REGISTER_EMAIL_VERIFICATION)
                .sendingType(OtpSendingType.EMAIL)
                .destination(params.getDestination())
                .build();
        var generatedOtp = generateOtpUseCase.execute(otpSendingRequest);
        if (generatedOtp.isSent()) {
            return generatedOtp;
        }
        otpSendingRequest.setOtp(generatedOtp.getOtp());
        // todo sent otp sms/email here
        OtpSendingType.fromType(OtpSendingType.EMAIL).getService().sendNotification(otpSendingRequest);
        return generatedOtp;
    }
}
