package com.gucardev.wallet.domain.otp.service;

import com.gucardev.wallet.domain.notification.sms.netgsm.NetGsmSendSmsUseCase;
import com.gucardev.wallet.domain.notification.sms.netgsm.dto.NetgsmGenerateOtpRequest;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.service.base.AbstractOtpSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsOtpSendUseCase extends AbstractOtpSendingService {


    private final NetGsmSendSmsUseCase netGsmSendSmsUseCase;

    @Override
    protected OtpSendingChannel getSendingChannel() {
        return OtpSendingChannel.SMS;
    }

    @Override
    protected void doSendNotification(OtpSendingRequest otpSendingRequest) {
        netGsmSendSmsUseCase.execute(new NetgsmGenerateOtpRequest(otpSendingRequest.getDestination(), otpSendingRequest.getOtp()));
    }
}