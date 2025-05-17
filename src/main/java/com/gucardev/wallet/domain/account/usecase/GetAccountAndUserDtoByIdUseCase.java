package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.mapper.AccountMapper;
import com.gucardev.wallet.domain.account.model.dto.AccountDtoWithUser;
import com.gucardev.wallet.domain.account.repository.AccountRepository;
import com.gucardev.wallet.domain.auth.helper.AuthorizationUtils;
import com.gucardev.wallet.domain.auth.usecase.GetAuthenticatedUserDtoUseCase;
import com.gucardev.wallet.domain.shared.enumeration.DeletedStatus;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildSilentException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAccountAndUserDtoByIdUseCase implements UseCaseWithParamsAndReturn<Long, AccountDtoWithUser> {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final GetAuthenticatedUserDtoUseCase getAuthenticatedUserDtoUseCase;

    @Override
    public AccountDtoWithUser execute(Long id) {
        var authenticatedUser = getAuthenticatedUserDtoUseCase.execute();

        // First get the account without deletion filter
        var account = accountRepository.findById(id, DeletedStatus.DELETED_UNKNOWN)
                .orElseThrow(() -> buildSilentException(ExceptionMessage.NOT_FOUND_EXCEPTION, id));

        // Check if user can access this account (considering deletion status)
        if (!AuthorizationUtils.canAccessDeletedResource(
                authenticatedUser,
                account.isDeleted(),
                authenticatedUser.getId(),
                account.getUser().getId())) {

            throw buildSilentException(ExceptionMessage.NOT_FOUND_EXCEPTION, id);
        }

        return accountMapper.toDtoWithUser(account);
    }
}