package com.gucardev.wallet.domain.account.model.dto;


import com.gucardev.wallet.domain.account.enumeration.AccountType;
import com.gucardev.wallet.domain.shared.model.dto.BaseDto;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoWithUser extends BaseDto {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private UserDto user;

}
