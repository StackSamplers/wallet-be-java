package com.gucardev.wallet.domain.otp.service;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsOtpSendUseCase  implements OtpSendingService {

    @Override
    public void sendNotification(OtpSendingRequest otpSendingRequest) {
        otpSendingRequest.setSendingType(OtpSendingType.SMS);

    }
}
