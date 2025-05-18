package com.gucardev.wallet.domain.auth.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Authority implements GrantedAuthority {
    // User authorities
    READ_USER("READ:USER"),
    WRITE_USER("WRITE:USER"),
    DELETE_USER("DELETE:USER"),
    DELETE_ACCOUNT("DELETE:ACCOUNT"),


    // Configuration authorities
    READ_CONFIGURATION("READ:CONFIGURATION"),
    WRITE_CONFIGURATION("WRITE:CONFIGURATION"),
    DELETE_CONFIGURATION("DELETE:CONFIGURATION"),

    // Report authorities
    READ_REPORT("READ:REPORT"),
    WRITE_REPORT("WRITE:REPORT");

    private final String permission;

    @Override
    public String getAuthority() {
        return permission;
    }
}
