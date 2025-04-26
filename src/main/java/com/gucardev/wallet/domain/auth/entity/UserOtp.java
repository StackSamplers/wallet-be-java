package com.gucardev.wallet.domain.auth.entity;

import com.gucardev.wallet.domain.auth.enumeration.OtpType;
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
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Enumerated(EnumType.STRING)
    private OtpType type;

    public UserOtp(String email, String otp, LocalDateTime expiryTime, OtpType type) {
        this.email = email;
        this.otp = otp;
        this.expiryTime = expiryTime;
        this.type = type;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}