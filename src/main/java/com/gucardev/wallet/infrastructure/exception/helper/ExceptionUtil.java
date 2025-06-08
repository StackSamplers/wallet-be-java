package com.gucardev.wallet.infrastructure.exception.helper;

import com.gucardev.wallet.infrastructure.config.message.MessageUtil;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.exception.model.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class ExceptionUtil {

    public ExceptionUtil() {
    }

    public static BusinessException buildException() {
        return buildException(ExceptionMessage.DEFAULT_EXCEPTION);
    }

    public static BusinessException buildException(String ex) {
        return new BusinessException(ex, ExceptionMessage.DEFAULT_EXCEPTION.getStatus(), ExceptionMessage.DEFAULT_EXCEPTION.getBusinessErrorCode());
    }

    public static BusinessException buildException(ExceptionMessage ex, Object... args) {
        String errorMessage = MessageUtil.getMessage(ex.getKey(), args);
        return new BusinessException(errorMessage, ex.getStatus(), ex.getBusinessErrorCode());
    }

    public static BusinessException buildException(ExceptionMessage ex) {
        String errorMessage = MessageUtil.getMessage(ex.getKey());
        return new BusinessException(errorMessage, ex.getStatus(), ex.getBusinessErrorCode());
    }
}