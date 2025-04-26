package com.gucardev.wallet.domain.notification.usecase;

import com.gucardev.wallet.domain.notification.model.OtpEmailRequest;
import com.gucardev.wallet.infrastructure.mail.dto.HtmlEmailRequest;
import com.gucardev.wallet.infrastructure.mail.service.EmailSenderService;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOTPMailUseCase implements UseCaseWithParams<OtpEmailRequest> {

    private final EmailSenderService emailSenderService;

    @Override
    public void execute(OtpEmailRequest params) {
        var htmlEmailRequest = new HtmlEmailRequest("OTP Verification", params.getReceiver(), "otp-mail");
        Map<String, Object> model = new HashMap<>();
        model.put("company", "My Company");
        model.put("otp_code", 352164);
        emailSenderService.sendTemplatedHtmlEmail(htmlEmailRequest, model);
    }
}
