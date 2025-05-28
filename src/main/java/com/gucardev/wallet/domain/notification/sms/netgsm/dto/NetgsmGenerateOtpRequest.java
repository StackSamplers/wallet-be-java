package com.gucardev.wallet.domain.notification.sms.netgsm.dto;

import com.gucardev.wallet.domain.otp.model.response.OtpResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetgsmGenerateOtpRequest {

    private String phoneNumber;
    private String otpCode;

    public NetgsmGenerateOtpRequest(OtpResponse generatedOtp) {
        this.otpCode = generatedOtp.getOtp();
        this.phoneNumber = generatedOtp.getPhoneNumber();
    }
}
