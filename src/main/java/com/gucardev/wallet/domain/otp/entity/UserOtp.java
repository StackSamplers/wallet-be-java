package com.gucardev.wallet.domain.otp.entity;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_otps")
@Getter
@Setter
@NoArgsConstructor
public class UserOtp extends BaseEntity {

    @Version
    private Long version;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Enumerated(EnumType.STRING)
    private OtpType type;

    @Enumerated(EnumType.STRING)
    private OtpSendingChannel sendingChannel;

    public UserOtp(String destination, String otp, LocalDateTime expiryTime, OtpType type, OtpSendingChannel sendingChannel) {
        this.destination = destination;
        this.otp = otp;
        this.expiryTime = expiryTime;
        this.type = type;
        this.sendingChannel = sendingChannel;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}