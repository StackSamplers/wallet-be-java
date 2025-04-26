package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.account.repository.AccountRepository;
import com.gucardev.wallet.infrastructure.exception.ExceptionMessage;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAccountByIdUseCase implements UseCaseWithParamsAndReturn<Long, Account> {

    private final AccountRepository accountRepository;

    @Override
    public Account execute(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));
    }
}
