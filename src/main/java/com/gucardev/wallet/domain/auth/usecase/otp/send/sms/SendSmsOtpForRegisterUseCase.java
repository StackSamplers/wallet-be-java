package com.gucardev.wallet.domain.auth.usecase.otp.send.sms;

import com.gucardev.wallet.domain.otp.enumeration.OtpSendingChannel;
import com.gucardev.wallet.domain.otp.enumeration.OtpType;
import com.gucardev.wallet.domain.otp.model.request.RegisterOtpRequest;
import com.gucardev.wallet.domain.otp.usecase.GenerateOtpUseCase;
import com.gucardev.wallet.domain.otp.usecase.base.AbstractSendOtpUseCase;
import com.gucardev.wallet.domain.user.usecase.GetUserByEmailUseCase;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;
import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;

@Slf4j
@Service
public class SendSmsOtpForRegisterUseCase extends AbstractSendOtpUseCase<RegisterOtpRequest> {

    private final GetUserByEmailUseCase getUserByEmailUseCase;

    public SendSmsOtpForRegisterUseCase(GenerateOtpUseCase generateOtpUseCase,
                                        GetUserByEmailUseCase getUserByEmailUseCase) {
        super(generateOtpUseCase);
        this.getUserByEmailUseCase = getUserByEmailUseCase;
    }

    @Override
    protected String extractDestination(RegisterOtpRequest params) {
        // Verify user exists
        var user = getUserByEmailUseCase.execute(params.getDestination())
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, params.getDestination()));

        if (user.getActivated().equals(true)) {
            throw buildSilentException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }
        if (user.getPhoneNumber().isBlank()) {
            throw buildSilentException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, params.getDestination());
        }
        return user.getPhoneNumber();
    }

    @Override
    protected OtpType getOtpType() {
        return OtpType.REGISTER_USER_VERIFICATION;
    }

    @Override
    protected OtpSendingChannel getSendingChannel() {
        return OtpSendingChannel.SMS;
    }

}