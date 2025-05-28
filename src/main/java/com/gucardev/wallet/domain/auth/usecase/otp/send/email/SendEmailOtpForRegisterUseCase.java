package com.gucardev.wallet.domain.auth.usecase.otp.send.email;

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
public class SendEmailOtpForRegisterUseCase extends AbstractSendOtpUseCase<RegisterOtpRequest> {

    private final GetUserByEmailUseCase getUserByEmailUseCase;

    public SendEmailOtpForRegisterUseCase(GenerateOtpUseCase generateOtpUseCase,
                                          GetUserByEmailUseCase getUserByEmailUseCase) {
        super(generateOtpUseCase);
        this.getUserByEmailUseCase = getUserByEmailUseCase;
    }

    @Override
    protected void validateRequest(RegisterOtpRequest params) {
        if (params == null || params.getDestination() == null) {
            throw buildException(ExceptionMessage.NOT_FOUND_EXCEPTION);
        }

        // Verify user exists
        var user = getUserByEmailUseCase.execute(params.getDestination())
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, params.getDestination()));

        if (user.getActivated().equals(true)) {
            throw buildSilentException(ExceptionMessage.INVALID_OTP_EXCEPTION);
        }

    }

    @Override
    protected String extractDestination(RegisterOtpRequest params) {
        return params.getDestination();
    }

    @Override
    protected OtpType getOtpType() {
        return OtpType.REGISTER_EMAIL_VERIFICATION;
    }

    @Override
    protected OtpSendingChannel getSendingChannel() {
        return OtpSendingChannel.EMAIL;
    }

}