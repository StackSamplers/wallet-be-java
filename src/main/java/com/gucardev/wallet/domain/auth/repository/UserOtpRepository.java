package com.gucardev.wallet.domain.auth.repository;

import com.gucardev.wallet.domain.auth.entity.UserOtp;
import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {

    Optional<UserOtp> findByEmailAndType(String email, OtpType type);

    boolean existsByEmailAndType(String email, OtpType type);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserOtp o WHERE o.expiryTime < :now")
    void deleteExpiredOtpS(LocalDateTime now);

}