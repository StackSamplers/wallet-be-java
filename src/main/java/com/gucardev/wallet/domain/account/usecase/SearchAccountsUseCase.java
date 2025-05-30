package com.gucardev.wallet.domain.account.usecase;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.account.mapper.AccountMapper;
import com.gucardev.wallet.domain.account.model.dto.AccountDto;
import com.gucardev.wallet.domain.account.model.request.AccountFilterRequest;
import com.gucardev.wallet.domain.account.repository.AccountRepository;
import com.gucardev.wallet.domain.account.repository.specification.AccountSpecification;
import com.gucardev.wallet.domain.auth.helper.AuthorizationUtils;
import com.gucardev.wallet.domain.auth.usecase.GetAuthenticatedUserDtoUseCase;
import com.gucardev.wallet.domain.shared.enumeration.DeletedStatus;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithParamsAndReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchAccountsUseCase implements UseCaseWithParamsAndReturn<AccountFilterRequest, Page<AccountDto>> {


    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final GetAuthenticatedUserDtoUseCase getAuthenticatedUserDtoUseCase;

    @Override
    public Page<AccountDto> execute(AccountFilterRequest params) {
        // Determine if user should see this account based on ownership and roles
        DeletedStatus deletedStatus = AuthorizationUtils.determineDeletedStatusFilterForAccount(getAuthenticatedUserDtoUseCase.execute());

        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getSortDir(), params.getSortBy()));

        Specification<Account> spec = Specification
                .where(AccountSpecification.hasEmailLike(params.getEmail()))
                .and(AccountSpecification.hasAccountNumberLike(params.getAccountNumber()))
                .and(AccountSpecification.hasAccountType(params.getAccountType()))
                .and(AccountSpecification.createdBetween(params.getStartDate(), params.getEndDate()))
                .and(AccountSpecification.deleted(deletedStatus));

        Page<Account> accountsPage = accountRepository.findAll(spec, pageable);
        return accountsPage.map(accountMapper::toDto);
    }
}
