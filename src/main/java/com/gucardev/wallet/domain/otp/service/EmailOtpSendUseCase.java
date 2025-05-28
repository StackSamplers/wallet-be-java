package com.gucardev.wallet.domain.otp.service;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.service.base.AbstractOtpSendingService;
import com.gucardev.wallet.infrastructure.mail.dto.HtmlEmailRequest;
import com.gucardev.wallet.infrastructure.mail.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailOtpSendUseCase extends AbstractOtpSendingService {

    private final EmailSenderService emailSenderService;

    @Override
    protected OtpSendingChannel getSendingChannel() {
        return OtpSendingChannel.EMAIL;
    }

    @Override
    protected void doSendNotification(OtpSendingRequest otpSendingRequest) {
        var htmlEmailRequest = new HtmlEmailRequest(
                "OTP Verification",
                otpSendingRequest.getDestination(),
                "otp-mail"
        );

        Map<String, Object> model = new HashMap<>();
        model.put("company", "My Company");
        model.put("otp_code", otpSendingRequest.getOtp());

        emailSenderService.sendTemplatedHtmlEmail(htmlEmailRequest, model);
    }
}