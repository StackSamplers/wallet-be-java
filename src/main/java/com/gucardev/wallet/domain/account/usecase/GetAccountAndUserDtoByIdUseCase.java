package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.mapper.AccountMapper;
import com.gucardev.wallet.domain.account.model.dto.AccountDtoWithUser;
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
public class GetAccountAndUserDtoByIdUseCase implements UseCaseWithParamsAndReturn<Long, AccountDtoWithUser> {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDtoWithUser execute(Long id) {
        return accountRepository.findById(id).map(accountMapper::toDtoWithUser)
                .orElseThrow(() -> buildException(ExceptionMessage.NOT_FOUND_EXCEPTION));
    }
}
