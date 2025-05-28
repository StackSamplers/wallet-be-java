package com.gucardev.wallet.domain.otp.service;

import com.gucardev.wallet.domain.notification.model.OtpEmailRequest;
import com.gucardev.wallet.domain.notification.usecase.SendOTPMailUseCase;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailOtpSendUseCase implements OtpSendingService {

    private final SendOTPMailUseCase sendOTPMailUseCase;

    @Override
    public void sendNotification(OtpSendingRequest otpSendingRequest) {
        otpSendingRequest.setSendingType(OtpSendingType.EMAIL);
        sendOTPMailUseCase.execute(new OtpEmailRequest(otpSendingRequest.getDestination(), otpSendingRequest.getOtp()));
    }
}
