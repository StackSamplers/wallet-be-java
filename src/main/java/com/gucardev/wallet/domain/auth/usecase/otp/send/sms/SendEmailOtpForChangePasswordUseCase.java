package com.gucardev.wallet.domain.auth.usecase.otp.send.sms;

import com.gucardev.wallet.domain.auth.model.request.ChangePasswordRequest;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingType;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
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
public class SendEmailOtpForChangePasswordUseCase implements UseCaseWithParamsAndReturn<ChangePasswordRequest, OtpResponse> {

    private final GenerateOtpUseCase generateOtpUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;

    @Override
    public OtpResponse execute(ChangePasswordRequest params) {
        String email = params.getEmail();

        // Check if email exists in the system
        var user = getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, email));

        OtpSendingRequest otpSendingRequest = OtpSendingRequest.builder()
                .type(OtpType.CHANGE_PASSWORD)
                .sendingType(OtpSendingType.SMS)
                .destination(user.getPhoneNumber())
                .build();
        var generatedOtp = generateOtpUseCase.execute(otpSendingRequest);
        if (generatedOtp.isSent()) {
            return generatedOtp;
        }
        otpSendingRequest.setOtp(generatedOtp.getOtp());
        // todo sent otp sms/email here
        OtpSendingType.fromType(OtpSendingType.SMS).getService().sendNotification(otpSendingRequest);
        return generatedOtp;
    }
}
