package com.gucardev.wallet.domain.user.mapper;

import com.gucardev.wallet.domain.account.entity.Account;
import com.gucardev.wallet.domain.account.model.dto.AccountDto;
import com.gucardev.wallet.domain.user.entity.User;
import com.gucardev.wallet.domain.user.enumeration.Role;
import com.gucardev.wallet.domain.user.model.request.UserCreateRequest;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateRequest request);

    @Mapping(target = "account", source = "account", qualifiedByName = "mapAccountWithUserId")
    @Mapping(target = "authorities", expression = "java(mapAuthorities(entity.getRoles()))")
    UserDto toDto(User entity);

    @Named("mapAccountWithUserId")
    @Mapping(target = "userId", source = "user.id")
    AccountDto toAccountDto(Account account);

    default Set<String> mapAuthorities(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

}
