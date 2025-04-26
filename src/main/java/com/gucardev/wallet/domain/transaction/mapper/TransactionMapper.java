package com.gucardev.wallet.domain.transaction.mapper;

import com.gucardev.wallet.domain.transaction.entity.Transaction;
import com.gucardev.wallet.domain.transaction.model.dto.TransactionDto;
import com.gucardev.wallet.domain.transaction.model.request.TransactionCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toEntity(TransactionCreateRequest request);

    @Mapping(source = "account.id", target = "accountId")
    TransactionDto toDto(Transaction entity);
}
