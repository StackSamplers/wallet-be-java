package com.gucardev.wallet.domain.account.mapper;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.account.model.dto.AccountDto;
import com.gucardev.wallet.domain.account.model.dto.AccountDtoWithUser;
import com.gucardev.wallet.domain.account.model.request.AccountCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountCreateRequest request);

    @Mapping(source = "user.id", target = "userId")
    AccountDto toDto(Account entity);

    @Mapping(target = "user.account", ignore = true)
    @Mapping(target = "user.authorities", ignore = true)
    AccountDtoWithUser toDtoWithUser(Account entity);

}
