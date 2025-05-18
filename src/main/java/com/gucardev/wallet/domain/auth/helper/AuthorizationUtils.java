package com.gucardev.wallet.domain.auth.helper;

import com.gucardev.wallet.domain.shared.enumeration.DeletedStatus;
import com.gucardev.wallet.domain.auth.enumeration.Authority;
import com.gucardev.wallet.domain.auth.enumeration.Role;
import com.gucardev.wallet.domain.user.model.response.UserDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for authorization checks related to resources, roles, and authorities.
 */
public final class AuthorizationUtils {

    private AuthorizationUtils() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isAuthorized(
            Long userResourceId,
            Long targetResourceId,
            UserDto user,
            Authority... authorities) {

        // Check if either ID is null - if so, they can't be equal
        if (userResourceId == null || targetResourceId == null) {
            return hasAnyAuthority(user, authorities);
        }

        return Objects.equals(userResourceId, targetResourceId) ||
                hasAnyAuthority(user, authorities);
    }

    public static boolean isAuthorized(
            Long userResourceId,
            Long targetResourceId,
            UserDto user,
            Role... roles) {

        return Objects.equals(userResourceId, targetResourceId) ||
                hasAnyRole(user, roles);
    }

    public static boolean hasAuthority(UserDto user, Authority authority) {
        if (user == null || user.getAuthorities() == null || authority == null) {
            return false;
        }

        return user.getAuthorities().contains(authority.getPermission());
    }

    public static boolean hasAnyAuthority(UserDto user, Authority... authorities) {
        if (user == null || user.getAuthorities() == null || authorities == null || authorities.length == 0) {
            return false;
        }

        return Arrays.stream(authorities)
                .map(Authority::getPermission)
                .anyMatch(user.getAuthorities()::contains);
    }

    public static boolean hasAllAuthorities(UserDto user, Authority... authorities) {
        if (user == null || user.getAuthorities() == null || authorities == null || authorities.length == 0) {
            return false;
        }

        return Arrays.stream(authorities)
                .map(Authority::getPermission)
                .allMatch(user.getAuthorities()::contains);
    }

    public static boolean hasRole(UserDto user, Role role) {
        if (user == null || user.getRoles() == null || role == null) {
            return false;
        }

        return user.getRoles().contains(role);
    }

    public static boolean hasAnyRole(UserDto user, Role... roles) {
        if (user == null || user.getRoles() == null || roles == null || roles.length == 0) {
            return false;
        }

        return Arrays.stream(roles)
                .anyMatch(user.getRoles()::contains);
    }

    public static DeletedStatus determineDeletedStatusFilterForAccount(UserDto user) {
        if (hasRole(user, Role.ADMIN)) {
            // Admins can see all records (including deleted)
            return DeletedStatus.DELETED_UNKNOWN;
        } else {
            // Regular users can only see non-deleted records of others
            return DeletedStatus.DELETED_FALSE;
        }
    }

    public static boolean canAccessDeletedResource(
            UserDto user,
            boolean isResourceDeleted,
            Long userResourceId,
            Long targetResourceId) {

        // If the resource is not deleted, allow access
        if (!isResourceDeleted) {
            return true;
        }

        // If the resource is deleted, only admin or owner can access
        return hasRole(user, Role.ADMIN) ||
                Objects.equals(userResourceId, targetResourceId);
    }

    public static Set<String> extractAuthoritiesFromRoles(Collection<Role> roles) {
        if (roles == null) {
            return Set.of();
        }

        return roles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(authority -> ((Authority) authority).getPermission())
                .collect(Collectors.toSet());
    }
}