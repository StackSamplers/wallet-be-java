package com.gucardev.wallet.domain.auth.usecase.otp;


import com.gucardev.wallet.domain.auth.entity.UserOtp;
import com.gucardev.wallet.domain.auth.enumeration.OtpType;
import com.gucardev.wallet.domain.auth.model.dto.OtpResponse;
import com.gucardev.wallet.domain.auth.model.request.GenerateOtpRequest;
import com.gucardev.wallet.domain.auth.repository.UserOtpRepository;
import com.gucardev.wallet.domain.user.usecase.GetUserByEmailUseCase;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;


@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateOtpUseCase implements UseCaseWithParamsAndReturn<GenerateOtpRequest, OtpResponse> {

    private final UserOtpRepository userOtpRepository;
    private final GetUserByEmailUseCase getUserByEmailUseCase;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 3;

    @Override
    @Transactional
    public OtpResponse execute(GenerateOtpRequest params) {
        String email = params.getEmail();
        OtpType type = params.getType();

        // Check if email exists in the system
        var user = getUserByEmailUseCase.execute(email)
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, email));

        // Check if OTP already exists for this email
        if (userOtpRepository.existsByEmailAndType(email, type)) {
            UserOtp existingOtp = userOtpRepository.findByEmailAndType(email, type)
                    .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION));

            // If not expired, return existing OTP
            if (!existingOtp.isExpired()) {
                return new OtpResponse(existingOtp.getOtp(), existingOtp.getExpiryTime(), existingOtp.getType(), user.getPhoneNumber(), true);
            }

            // If expired, delete it so we can create a new one
            userOtpRepository.delete(existingOtp);
        }

        // Generate new OTP
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        // Save OTP
        UserOtp userOtp = new UserOtp(email, otp, expiryTime, type);
        userOtpRepository.save(userOtp);

        var response = new OtpResponse(otp, expiryTime, type, user.getPhoneNumber(), false);

        log.info("OTP created successfully for email: {}, otp: {}", params.getEmail(), response);
        return response;
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}