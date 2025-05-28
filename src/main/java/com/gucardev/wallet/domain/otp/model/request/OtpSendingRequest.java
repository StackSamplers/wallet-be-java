package com.gucardev.wallet.domain.otp.model.request;

import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpSendingRequest {

    private String destination;
    private String otp;

    private OtpType type;
    private OtpSendingChannel sendingChannel;

}
