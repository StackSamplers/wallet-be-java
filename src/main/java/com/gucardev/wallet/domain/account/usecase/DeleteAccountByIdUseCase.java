package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.repository.AccountRepository;
import com.gucardev.wallet.domain.auth.helper.AuthorizationUtils;
import com.gucardev.wallet.domain.auth.usecase.GetAuthenticatedUserDtoUseCase;
import com.gucardev.wallet.domain.auth.enumeration.Authority;
import com.gucardev.wallet.infrastructure.config.constants.Constants;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteAccountByIdUseCase implements UseCaseWithParams<Long> {

    private final AccountRepository accountRepository;
    private final GetAuthenticatedUserDtoUseCase getAuthenticatedUserDtoUseCase;

    @Override
    public void execute(Long id) {
        var authenticatedUser = getAuthenticatedUserDtoUseCase.execute();

        var account = accountRepository.findById(id)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION, id));

        // Direct ID comparison - you provide exactly which IDs to compare
        if (AuthorizationUtils.isAuthorized(
                account.getUser().getId(),               // User's account ID
                id,                                      // Target account ID
                authenticatedUser,                       // User object for authority check
                Authority.DELETE_ACCOUNT)) {             // Authorities to allow
            accountRepository.softDelete(id, Constants.DEFAULT_AUDITOR);
        } else {
            throw buildException(ExceptionMessage.FORBIDDEN_EXCEPTION);
        }
    }

}
