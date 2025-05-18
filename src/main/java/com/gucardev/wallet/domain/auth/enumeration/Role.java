package com.gucardev.wallet.domain.auth.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ROLE_ADMIN", Set.of(
            Authority.READ_USER, Authority.WRITE_USER, Authority.DELETE_USER,
            Authority.READ_CONFIGURATION, Authority.WRITE_CONFIGURATION, Authority.DELETE_CONFIGURATION,
            Authority.READ_REPORT, Authority.WRITE_REPORT
    )),

    STAFF("ROLE_STAFF", Set.of(
            Authority.READ_USER,
            Authority.READ_CONFIGURATION,
            Authority.READ_REPORT, Authority.WRITE_REPORT
    )),

    MACHINE("ROLE_MACHINE", Set.of(
            Authority.READ_USER,
            Authority.READ_CONFIGURATION,
            Authority.READ_REPORT, Authority.WRITE_REPORT
    )),

    USER("ROLE_USER", Set.of(
            Authority.READ_USER,
            Authority.READ_REPORT, Authority.WRITE_CONFIGURATION
    ));

    private final String roleName;
    private final Set<Authority> authorities;

    @Override
    public String getAuthority() {
        return roleName;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<>(authorities);
    }
}
