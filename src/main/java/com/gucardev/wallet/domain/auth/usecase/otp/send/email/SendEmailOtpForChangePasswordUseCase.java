package com.gucardev.wallet.domain.auth.usecase.otp.send.email;

import com.gucardev.wallet.domain.auth.model.request.ChangePasswordRequest;
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
import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
public class SendEmailOtpForChangePasswordUseCase extends AbstractSendOtpUseCase<ChangePasswordRequest> {

    private final GetUserByEmailUseCase getUserByEmailUseCase;

    public SendEmailOtpForChangePasswordUseCase(GenerateOtpUseCase generateOtpUseCase,
                                                GetUserByEmailUseCase getUserByEmailUseCase) {
        super(generateOtpUseCase);
        this.getUserByEmailUseCase = getUserByEmailUseCase;
    }

    @Override
    protected String extractDestination(ChangePasswordRequest params) {
        // Verify user exists
        var user = getUserByEmailUseCase.execute(params.getEmail())
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, params.getEmail()));
        if (user.getActivated().equals(true)) {
            throw buildException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }
        return params.getEmail();
    }

    @Override
    protected OtpType getOtpType() {
        return OtpType.CHANGE_PASSWORD;
    }

    @Override
    protected OtpSendingChannel getSendingChannel() {
        return OtpSendingChannel.EMAIL;
    }

}