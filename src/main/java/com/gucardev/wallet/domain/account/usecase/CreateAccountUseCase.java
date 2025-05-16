package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.account.mapper.AccountMapper;
import com.gucardev.wallet.domain.account.model.dto.AccountDto;
import com.gucardev.wallet.domain.account.model.request.AccountCreateRequest;
import com.gucardev.wallet.domain.account.repository.AccountRepository;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.usecase.GetUserByIdUseCase;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAccountUseCase implements UseCaseWithParamsAndReturn<AccountCreateRequest, AccountDto> {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final GetUserByIdUseCase getUserByIdUseCase;

    @Override
    @Transactional
    public AccountDto execute(AccountCreateRequest params) {
        var accNumber = UUID.randomUUID().toString().replace("-", "");
        log.debug("Creating new account with number: {}", accNumber);

        User user = getUserByIdUseCase.execute(params.getUserId())
                .orElseThrow(() -> buildException(ExceptionMessage.USER_NOT_FOUND_EXCEPTION, params.getUserId()));

        // Use the helper method to check if user has an account
        if (user.hasAccount()) {
            throw buildException(ExceptionMessage.ALREADY_EXISTS_EXCEPTION);
        }

        Account newAccount = accountMapper.toEntity(params);
        newAccount.setBalance(params.getInitialBalance());
        newAccount.setAccountNumber(accNumber);

        // Use the helper method to set bidirectional relationship
        user.addAccount(newAccount);

        // No need to explicitly set newAccount.setUser(user) as it's done in the helper method

        Account savedAccount = accountRepository.save(newAccount);
        log.debug("Saved account with ID: {}", savedAccount.getId());

        return accountMapper.toDto(savedAccount);
    }
}