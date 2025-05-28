package com.gucardev.wallet.domain.otp.service;

import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.infrastructure.mail.dto.HtmlEmailRequest;
import com.gucardev.wallet.infrastructure.mail.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailOtpSendUseCase implements OtpSendingService {

    private final EmailSenderService emailSenderService;

    @Override
    public void sendNotification(OtpSendingRequest otpSendingRequest) {
        var htmlEmailRequest = new HtmlEmailRequest("OTP Verification", otpSendingRequest.getDestination(), "otp-mail");
        Map<String, Object> model = new HashMap<>();
        model.put("company", "My Company");
        model.put("otp_code", otpSendingRequest.getOtp());
        emailSenderService.sendTemplatedHtmlEmail(htmlEmailRequest, model);
    }
}
