package com.gucardev.wallet.domain.otp.repository;

import com.gucardev.wallet.domain.otp.entity.UserOtp;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {

    Optional<UserOtp> findByDestinationAndTypeAndSendingChannel(String destination, OtpType type, OtpSendingChannel sendingChannel);

    boolean existsByDestinationAndTypeAndSendingChannel(String destination, OtpType type, OtpSendingChannel sendingChannel);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserOtp o WHERE o.expiryTime < :now")
    void deleteExpiredOtpS(LocalDateTime now);

}