package com.gucardev.wallet.domain.otp.service;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.service.base.AbstractOtpSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsOtpSendUseCase extends AbstractOtpSendingService {

    // Inject SMS service here
    // private final SmsService smsService;
    @Override
    protected OtpSendingChannel getSendingChannel() {
        return OtpSendingChannel.SMS;
    }

    @Override
    protected void doSendNotification(OtpSendingRequest otpSendingRequest) {

    }
}