package com.gucardev.wallet.domain.notification.sms.netgsm;

import com.gucardev.wallet.domain.notification.sms.netgsm.client.NetGSMSmsApi;
import com.gucardev.wallet.domain.notification.sms.netgsm.dto.NetgsmGenerateOtpRequest;
import com.gucardev.wallet.domain.notification.sms.netgsm.client.model.request.NetgsmOtpRequest;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NetGsmSendSmsUseCase implements UseCaseWithParamsAndReturn<NetgsmGenerateOtpRequest, String> {

    private final NetGSMSmsApi netgsmFeignClient;

    private final static String OTP_MESSAGE = "OTP dogrulama kodunuz: %s";

    @Value("${app-specific-configs.netgsm.usercode}")
    private String usercode;

    @Value("${app-specific-configs.netgsm.password}")
    private String password;

    @Value("${app-specific-configs.netgsm.msgheader}")
    private String msgheader;

    @Override
    public String execute(NetgsmGenerateOtpRequest params) {
        var otpCode = params.getOtpCode();
        var phoneNumber = params.getPhoneNumber();
        NetgsmOtpRequest request = NetgsmOtpRequest.builder()
                .header(NetgsmOtpRequest.Header.builder()
                        .usercode(usercode)
                        .password(password)
                        .msgheader(msgheader)
                        .build())
                .body(NetgsmOtpRequest.Body.builder()
                        .msg(OTP_MESSAGE.formatted(otpCode))
                        .no(phoneNumber)
                        .build())
                .build();
        return netgsmFeignClient.sendOtpSms(request);
    }
}