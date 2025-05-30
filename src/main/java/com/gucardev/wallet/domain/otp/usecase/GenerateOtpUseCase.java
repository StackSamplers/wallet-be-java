package com.gucardev.wallet.domain.otp.usecase;

import com.gucardev.wallet.domain.otp.entity.UserOtp;
import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.model.request.OtpSendingRequest;
import com.gucardev.wallet.domain.otp.model.response.OtpResponse;
import com.gucardev.wallet.domain.otp.repository.UserOtpRepository;
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
public class GenerateOtpUseCase implements UseCaseWithParamsAndReturn<OtpSendingRequest, OtpResponse> {

    private final UserOtpRepository userOtpRepository;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 3;

    @Override
    @Transactional
    public OtpResponse execute(OtpSendingRequest params) {
        String destination = params.getDestination();
        OtpType type = params.getType();
        OtpSendingChannel sendingChannel = params.getSendingChannel();

        // Check if OTP already exists for this destination
        if (userOtpRepository.existsByDestinationAndTypeAndSendingChannel(destination, type, sendingChannel)) {
            UserOtp existingOtp = userOtpRepository.findByDestinationAndTypeAndSendingChannel(destination, type, sendingChannel)
                    .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));

            // If not expired, return existing OTP
            if (!existingOtp.isExpired()) {
                log.info("Returning existing OTP for destination: {}", destination);
                return new OtpResponse(existingOtp.getOtp(), existingOtp.getExpiryTime(), existingOtp.getType(), destination, true);
            }

            // If expired, delete it so we can create a new one
            userOtpRepository.delete(existingOtp);
            log.info("Deleted expired OTP for destination: {}", destination);
        }

        // Generate new OTP
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        // Save OTP
        UserOtp userOtp = new UserOtp(destination, otp, expiryTime, type, sendingChannel);
        userOtpRepository.save(userOtp);

        var response = new OtpResponse(otp, expiryTime, type, destination, false);

        log.info("OTP generated successfully for destination: {}, type: {}, sendingChannel: {}",
                destination, type, sendingChannel);
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