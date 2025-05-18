package com.gucardev.wallet.domain.auth.helper;

import static org.junit.jupiter.api.Assertions.*;

import com.gucardev.wallet.domain.account.model.dto.AccountDto;
import com.gucardev.wallet.domain.auth.enumeration.Authority;
import com.gucardev.wallet.domain.auth.enumeration.Role;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class AuthorizationUtilsTest {

    @Test
    @DisplayName("Constructor should throw UnsupportedOperationException when instantiated")
    void constructorShouldThrowException() {
        assertThrows(Exception.class, () -> {
            // Using reflection to call the private constructor
            java.lang.reflect.Constructor<AuthorizationUtils> constructor =
                    AuthorizationUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Nested
    @DisplayName("isAuthorized with Authority tests")
    class IsAuthorizedWithAuthorityTests {

        @Test
        @DisplayName("Should return true when userResourceId equals targetResourceId")
        void shouldReturnTrueWhenUserResourceIdEqualsTargetResourceId() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 1L;
            UserDto user = new UserDto();

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when user has required authority")
        void shouldReturnTrueWhenUserHasRequiredAuthority() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 2L;
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when userResourceId doesn't equal targetResourceId and user doesn't have required authority")
        void shouldReturnFalseWhenUserResourceIdNotEqualsTargetResourceIdAndUserDoesntHaveRequiredAuthority() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 2L;
            UserDto user = createUserWithAuthorities(Authority.WRITE_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when IDs don't match, user is null")
        void shouldReturnFalseWhenIdsDoNotMatchAndUserIsNull() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 2L;
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true when IDs match, user is null")
        void shouldReturnTrueWhenIdsMatchAndUserIsNull() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 1L;
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when both IDs are null")
        void shouldReturnFalseWhenBothIdsAreNull() {
            // Given
            Long userResourceId = null;
            Long targetResourceId = null;
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            // Java's Objects.equals considers two nulls to be equal
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when one ID is null but user has required authority")
        void shouldReturnTrueWhenOneIdIsNullButUserHasRequiredAuthority() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = null;
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Authority.READ_USER);

            // Then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("isAuthorized with Role tests")
    class IsAuthorizedWithRoleTests {

        @Test
        @DisplayName("Should return true when userResourceId equals targetResourceId")
        void shouldReturnTrueWhenUserResourceIdEqualsTargetResourceId() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 1L;
            UserDto user = new UserDto();

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Role.ADMIN);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when user has required role")
        void shouldReturnTrueWhenUserHasRequiredRole() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 2L;
            UserDto user = createUserWithRoles(Role.ADMIN);

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Role.ADMIN);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when userResourceId doesn't equal targetResourceId and user doesn't have required role")
        void shouldReturnFalseWhenUserResourceIdNotEqualsTargetResourceIdAndUserDoesntHaveRequiredRole() {
            // Given
            Long userResourceId = 1L;
            Long targetResourceId = 2L;
            UserDto user = createUserWithRoles(Role.USER);

            // When
            boolean result = AuthorizationUtils.isAuthorized(
                    userResourceId, targetResourceId, user, Role.ADMIN);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("hasAuthority tests")
    class HasAuthorityTests {

        @Test
        @DisplayName("Should return true when user has the authority")
        void shouldReturnTrueWhenUserHasAuthority() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAuthority(user, Authority.READ_USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when user doesn't have the authority")
        void shouldReturnFalseWhenUserDoesntHaveAuthority() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.WRITE_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAuthority(user, Authority.READ_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user is null")
        void shouldReturnFalseWhenUserIsNull() {
            // Given
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.hasAuthority(user, Authority.READ_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user authorities is null")
        void shouldReturnFalseWhenUserAuthoritiesIsNull() {
            // Given
            UserDto user = new UserDto();
            user.setAuthorities(null);

            // When
            boolean result = AuthorizationUtils.hasAuthority(user, Authority.READ_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when authority is null")
        void shouldReturnFalseWhenAuthorityIsNull() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAuthority(user, null);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("hasAnyAuthority tests")
    class HasAnyAuthorityTests {

        @Test
        @DisplayName("Should return true when user has one of the authorities")
        void shouldReturnTrueWhenUserHasOneOfTheAuthorities() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAnyAuthority(
                    user, Authority.READ_USER, Authority.WRITE_USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when user doesn't have any of the authorities")
        void shouldReturnFalseWhenUserDoesntHaveAnyOfTheAuthorities() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.DELETE_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAnyAuthority(
                    user, Authority.READ_USER, Authority.WRITE_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user is null")
        void shouldReturnFalseWhenUserIsNull() {
            // Given
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.hasAnyAuthority(
                    user, Authority.READ_USER, Authority.WRITE_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when authorities array is null")
        void shouldReturnFalseWhenAuthoritiesArrayIsNull() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAnyAuthority(user, (Authority[]) null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when authorities array is empty")
        void shouldReturnFalseWhenAuthoritiesArrayIsEmpty() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAnyAuthority(user);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("hasAllAuthorities tests")
    class HasAllAuthoritiesTests {

        @Test
        @DisplayName("Should return true when user has all authorities")
        void shouldReturnTrueWhenUserHasAllAuthorities() {
            // Given
            UserDto user = createUserWithAuthorities(
                    Authority.READ_USER.getPermission(),
                    Authority.WRITE_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAllAuthorities(
                    user, Authority.READ_USER, Authority.WRITE_USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when user doesn't have all authorities")
        void shouldReturnFalseWhenUserDoesntHaveAllAuthorities() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAllAuthorities(
                    user, Authority.READ_USER, Authority.WRITE_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user is null")
        void shouldReturnFalseWhenUserIsNull() {
            // Given
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.hasAllAuthorities(
                    user, Authority.READ_USER, Authority.WRITE_USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when authorities array is null")
        void shouldReturnFalseWhenAuthoritiesArrayIsNull() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAllAuthorities(user, (Authority[]) null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when authorities array is empty")
        void shouldReturnFalseWhenAuthoritiesArrayIsEmpty() {
            // Given
            UserDto user = createUserWithAuthorities(Authority.READ_USER.getPermission());

            // When
            boolean result = AuthorizationUtils.hasAllAuthorities(user);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("hasRole tests")
    class HasRoleTests {

        @Test
        @DisplayName("Should return true when user has the role")
        void shouldReturnTrueWhenUserHasRole() {
            // Given
            UserDto user = createUserWithRoles(Role.ADMIN);

            // When
            boolean result = AuthorizationUtils.hasRole(user, Role.ADMIN);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when user doesn't have the role")
        void shouldReturnFalseWhenUserDoesntHaveRole() {
            // Given
            UserDto user = createUserWithRoles(Role.USER);

            // When
            boolean result = AuthorizationUtils.hasRole(user, Role.ADMIN);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user is null")
        void shouldReturnFalseWhenUserIsNull() {
            // Given
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.hasRole(user, Role.ADMIN);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user roles is null")
        void shouldReturnFalseWhenUserRolesIsNull() {
            // Given
            UserDto user = new UserDto();
            user.setRoles(null);

            // When
            boolean result = AuthorizationUtils.hasRole(user, Role.ADMIN);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when role is null")
        void shouldReturnFalseWhenRoleIsNull() {
            // Given
            UserDto user = createUserWithRoles(Role.ADMIN);

            // When
            boolean result = AuthorizationUtils.hasRole(user, null);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("hasAnyRole tests")
    class HasAnyRoleTests {

        @Test
        @DisplayName("Should return true when user has one of the roles")
        void shouldReturnTrueWhenUserHasOneOfTheRoles() {
            // Given
            UserDto user = createUserWithRoles(Role.ADMIN);

            // When
            boolean result = AuthorizationUtils.hasAnyRole(
                    user, Role.ADMIN, Role.USER);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when user doesn't have any of the roles")
        void shouldReturnFalseWhenUserDoesntHaveAnyOfTheRoles() {
            // Given
            UserDto user = createUserWithRoles(Role.STAFF);

            // When
            boolean result = AuthorizationUtils.hasAnyRole(
                    user, Role.ADMIN, Role.USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when user is null")
        void shouldReturnFalseWhenUserIsNull() {
            // Given
            UserDto user = null;

            // When
            boolean result = AuthorizationUtils.hasAnyRole(
                    user, Role.ADMIN, Role.USER);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when roles array is null")
        void shouldReturnFalseWhenRolesArrayIsNull() {
            // Given
            UserDto user = createUserWithRoles(Role.ADMIN);

            // When
            boolean result = AuthorizationUtils.hasAnyRole(user, (Role[]) null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when roles array is empty")
        void shouldReturnFalseWhenRolesArrayIsEmpty() {
            // Given
            UserDto user = createUserWithRoles(Role.ADMIN);

            // When
            boolean result = AuthorizationUtils.hasAnyRole(user);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("canAccessDeletedResource tests")
    class CanAccessDeletedResourceTests {

        @Test
        @DisplayName("Should return true when resource is not deleted")
        void shouldReturnTrueWhenResourceIsNotDeleted() {
            // Given
            UserDto user = new UserDto();
            boolean isResourceDeleted = false;
            Long userResourceId = 1L;
            Long targetResourceId = 2L;

            // When
            boolean result = AuthorizationUtils.canAccessDeletedResource(
                    user, isResourceDeleted, userResourceId, targetResourceId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when resource is deleted and user is admin")
        void shouldReturnTrueWhenResourceIsDeletedAndUserIsAdmin() {
            // Given
            UserDto user = createUserWithRoles(Role.ADMIN);
            boolean isResourceDeleted = true;
            Long userResourceId = 1L;
            Long targetResourceId = 2L;

            // When
            boolean result = AuthorizationUtils.canAccessDeletedResource(
                    user, isResourceDeleted, userResourceId, targetResourceId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when resource is deleted and user is owner")
        void shouldReturnTrueWhenResourceIsDeletedAndUserIsOwner() {
            // Given
            UserDto user = createUserWithRoles(Role.USER);
            boolean isResourceDeleted = true;
            Long userResourceId = 1L;
            Long targetResourceId = 1L;

            // When
            boolean result = AuthorizationUtils.canAccessDeletedResource(
                    user, isResourceDeleted, userResourceId, targetResourceId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when resource is deleted and user is not admin or owner")
        void shouldReturnFalseWhenResourceIsDeletedAndUserIsNotAdminOrOwner() {
            // Given
            UserDto user = createUserWithRoles(Role.USER);
            boolean isResourceDeleted = true;
            Long userResourceId = 1L;
            Long targetResourceId = 2L;

            // When
            boolean result = AuthorizationUtils.canAccessDeletedResource(
                    user, isResourceDeleted, userResourceId, targetResourceId);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when resource is deleted, user is null")
        void shouldReturnFalseWhenResourceIsDeletedAndUserIsNull() {
            // Given
            UserDto user = null;
            boolean isResourceDeleted = true;
            Long userResourceId = 1L;
            Long targetResourceId = 2L;

            // When
            boolean result = AuthorizationUtils.canAccessDeletedResource(
                    user, isResourceDeleted, userResourceId, targetResourceId);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true when resource is deleted, user is null, but IDs match")
        void shouldReturnTrueWhenResourceIsDeletedUserIsNullButIdsMatch() {
            // Given
            UserDto user = null;
            boolean isResourceDeleted = true;
            Long userResourceId = 1L;
            Long targetResourceId = 1L;

            // When
            boolean result = AuthorizationUtils.canAccessDeletedResource(
                    user, isResourceDeleted, userResourceId, targetResourceId);

            // Then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("extractAuthoritiesFromRoles tests")
    class ExtractAuthoritiesFromRolesTests {

        @Test
        @DisplayName("Should extract all authorities from roles")
        void shouldExtractAllAuthoritiesFromRoles() {
            // Given
            Set<Role> roles = Set.of(Role.ADMIN, Role.USER);

            // When
            Set<String> result = AuthorizationUtils.extractAuthoritiesFromRoles(roles);

            // Then
            // ADMIN should have all authorities
            assertTrue(result.contains(Authority.READ_USER.getPermission()));
            assertTrue(result.contains(Authority.WRITE_USER.getPermission()));
            assertTrue(result.contains(Authority.DELETE_USER.getPermission()));
            // Check a few more to ensure they are included
            assertTrue(result.contains(Authority.READ_REPORT.getPermission()));
            assertTrue(result.contains(Authority.WRITE_REPORT.getPermission()));
        }

        @Test
        @DisplayName("Should return empty set when roles is null")
        void shouldReturnEmptySetWhenRolesIsNull() {
            // Given
            Set<Role> roles = null;

            // When
            Set<String> result = AuthorizationUtils.extractAuthoritiesFromRoles(roles);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty set when roles is empty")
        void shouldReturnEmptySetWhenRolesIsEmpty() {
            // Given
            Set<Role> roles = Collections.emptySet();

            // When
            Set<String> result = AuthorizationUtils.extractAuthoritiesFromRoles(roles);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    // Helper methods to create test objects

    private UserDto createUserWithAuthorities(String... authorities) {
        UserDto user = new UserDto();
        Set<String> authoritySet = new HashSet<>();
        Collections.addAll(authoritySet, authorities);
        user.setAuthorities(authoritySet);
        return user;
    }

    private UserDto createUserWithRoles(Role... roles) {
        UserDto user = new UserDto();
        Set<Role> roleSet = new HashSet<>();
        Collections.addAll(roleSet, roles);
        user.setRoles(roleSet);
        return user;
    }

    private UserDto createUserWithAccountId(Long accountId) {
        UserDto user = new UserDto();
        AccountDto account = new AccountDto();
        account.setId(accountId);
        user.setAccount(account);
        return user;
    }
}