package com.gucardev.wallet.domain.auth.controller;

import com.gucardev.wallet.domain.auth.model.request.ChangePasswordConfirmRequest;
import com.gucardev.wallet.domain.auth.model.request.ChangePasswordRequest;
import com.gucardev.wallet.domain.auth.model.request.UserRegisterRequest;
import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.auth.model.response.LoginRequest;
import com.gucardev.wallet.domain.auth.model.response.RefreshTokenRequest;
import com.gucardev.wallet.domain.auth.model.response.RolePermissionsDto;
import com.gucardev.wallet.domain.auth.model.response.TokenDto;
import com.gucardev.wallet.domain.auth.usecase.*;
import com.gucardev.wallet.domain.auth.usecase.otp.confirm.email.ActivateUserByEmailOtpUseCase;
import com.gucardev.wallet.domain.auth.usecase.otp.confirm.email.ChangePasswordAfterValidateEmailOtpUseCase;
import com.gucardev.wallet.domain.auth.usecase.otp.send.email.SendEmailOtpForChangePasswordUseCase;
import com.gucardev.wallet.domain.auth.usecase.otp.send.email.SendEmailOtpForRegisterUseCase;
import com.gucardev.wallet.domain.auth.usecase.token.GenerateTokenByRefreshTokenUseCase;
import com.gucardev.wallet.domain.otp.model.request.RegisterOtpRequest;
import com.gucardev.wallet.domain.otp.model.response.OtpResponse;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import com.gucardev.wallet.infrastructure.response.SuccessResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final ActivateUserByEmailOtpUseCase activateUserByEmailOtpUseCase;
    private final ChangePasswordAfterValidateEmailOtpUseCase changePasswordAfterValidateEmailOtpUseCase;
    private final SendEmailOtpForRegisterUseCase sendEmailOtpForRegisterUseCase;
    private final SendEmailOtpForChangePasswordUseCase sendEmailOtpForChangePasswordUseCase;
    private final GenerateTokenByRefreshTokenUseCase generateTokenByRefreshTokenUseCase;
    private final GetRolesAndPermissionsUseCase getRolesAndPermissionsUseCase;

    // ===== AUTHENTICATION ENDPOINTS =====

    @Operation(
            summary = "User login",
            description = "Authenticate user and retrieve JWT token"
    )
    @RateLimiter(name = "loginRateLimiter")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        return SuccessResponse.builder()
                .body(loginUserUseCase.execute(loginRequest))
                .build();
    }

    @Operation(
            summary = "Refresh JWT token",
            description = "Generate new JWT token using refresh token"
    )
    @RateLimiter(name = "loginRateLimiter")
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDto> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return SuccessResponse.builder()
                .body(generateTokenByRefreshTokenUseCase.execute(refreshTokenRequest))
                .build();
    }

    // ===== USER REGISTRATION ENDPOINTS =====

    @Operation(
            summary = "Register new user",
            description = "Create a new user account"
    )
    @RateLimiter(name = "registerRateLimiter")
    @PostMapping("/register")
    public ResponseEntity<TokenDto> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        registerUserUseCase.execute(userRegisterRequest);
        return SuccessResponse.builder().build();
    }

    // ===== USER PROFILE ENDPOINTS =====

    @Operation(
            summary = "Get current user profile",
            description = "Retrieve authenticated user information"
    )
    @GetMapping("/me")  // Standard convention for current user
    public ResponseEntity<UserDto> getCurrentUser() {
        return SuccessResponse.builder()
                .body(getAuthenticatedUserUseCase.execute())
                .build();
    }

    // ===== OTP ENDPOINTS =====

    @Operation(
            summary = "Send registration OTP",
            description = "Generate and send 6-digit OTP for email verification during registration. Valid for 3 minutes."
    )
    @PostMapping("/otp/email/registration/send")  // Grouped under /otp with clear purpose
    public ResponseEntity<OtpResponse> sendRegistrationOtp(@Valid @RequestBody RegisterOtpRequest request) {
        sendEmailOtpForRegisterUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "Verify registration OTP",
            description = "Validate OTP and activate user account"
    )
    @PostMapping("/otp/email/registration/verify")
    public ResponseEntity<Void> verifyRegistrationOtp(@Valid @RequestBody ValidateOtpRequest request) {
        activateUserByEmailOtpUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    // ===== PASSWORD RESET ENDPOINTS =====

    @Operation(
            summary = "Send password reset OTP",
            description = "Generate and send 6-digit OTP for password reset. Valid for 3 minutes."
    )
    @PostMapping("/otp/email/password-reset")  // Clear password reset flow
    public ResponseEntity<OtpResponse> sendPasswordResetOtp(@Valid @RequestBody ChangePasswordRequest request) {
        sendEmailOtpForChangePasswordUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "Reset password with OTP",
            description = "Change password after OTP verification"
    )
    @PostMapping("/otp/email/password-reset-confirm")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ChangePasswordConfirmRequest request) {
        changePasswordAfterValidateEmailOtpUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    // ===== AUTHORIZATION ENDPOINTS =====

    @Operation(
            summary = "Get roles and permissions",
            description = "Retrieve all available roles and their associated permissions"
    )
    @GetMapping("/roles")
    public ResponseEntity<List<RolePermissionsDto>> getRolesAndPermissions() {
        return SuccessResponse.builder()
                .body(getRolesAndPermissionsUseCase.execute())
                .build();
    }
}
