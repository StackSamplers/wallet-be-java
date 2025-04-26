package com.gucardev.wallet.domain.notification.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpEmailRequest {

    private String receiver;
    private String otp;

}
