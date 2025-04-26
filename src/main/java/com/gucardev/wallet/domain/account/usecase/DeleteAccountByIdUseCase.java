package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.repository.AccountRepository;
import com.gucardev.wallet.infrastructure.config.constants.Constants;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteAccountByIdUseCase implements UseCaseWithParams<Long> {

    private final AccountRepository accountRepository;

    @Override
    public void execute(Long id) {
        accountRepository.softDelete(id, Constants.DEFAULT_AUDITOR);
    }

}
