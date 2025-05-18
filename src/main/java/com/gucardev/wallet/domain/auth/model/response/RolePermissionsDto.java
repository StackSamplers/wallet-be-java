package com.gucardev.wallet.domain.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionsDto {
    private String roleName;
    private Set<String> permissions;
}