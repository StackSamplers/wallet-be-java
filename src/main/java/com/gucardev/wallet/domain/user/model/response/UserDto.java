package com.gucardev.wallet.domain.user.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gucardev.wallet.domain.account.model.dto.AccountDto;
import com.gucardev.wallet.domain.shared.model.dto.BaseDto;
import com.gucardev.wallet.domain.auth.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {

    private Long id;
    @JsonIgnore
    private String password;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private Set<Role> roles = new HashSet<>();
    private Set<String> authorities = new HashSet<>();

    private AccountDto account;

}
