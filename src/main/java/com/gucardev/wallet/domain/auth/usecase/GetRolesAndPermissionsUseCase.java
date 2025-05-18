package com.gucardev.wallet.domain.auth.usecase;

import com.gucardev.wallet.domain.auth.enumeration.Authority;
import com.gucardev.wallet.domain.auth.enumeration.Role;
import com.gucardev.wallet.domain.auth.model.response.RolePermissionsDto;
import com.gucardev.wallet.infrastructure.usecase.UseCaseWithReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRolesAndPermissionsUseCase implements UseCaseWithReturn<List<RolePermissionsDto>> {

    @Override
    public List<RolePermissionsDto> execute() {
        return Arrays.stream(Role.values())
                .map(role -> RolePermissionsDto.builder()
                        .roleName(role.name())
                        .permissions(role.getAuthorities().stream()
                                .map(authority -> ((Authority) authority).name())
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }
}
