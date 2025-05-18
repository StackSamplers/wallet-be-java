package com.gucardev.wallet.domain.auth.controller;

import com.gucardev.wallet.domain.auth.model.response.*;
import com.gucardev.wallet.domain.auth.model.request.ChangePasswordRequest;
import com.gucardev.wallet.domain.auth.model.request.GenerateOtpRequest;
import com.gucardev.wallet.domain.auth.model.request.UserRegisterRequest;
import com.gucardev.wallet.domain.auth.model.request.ValidateOtpRequest;
import com.gucardev.wallet.domain.auth.usecase.*;
import com.gucardev.wallet.domain.auth.usecase.otp.ChangePasswordOtpValidateUseCase;
import com.gucardev.wallet.domain.auth.usecase.otp.SendOtpForChangePasswordUseCase;
import com.gucardev.wallet.domain.auth.usecase.otp.SendOtpForRegisterUseCase;
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

import static com.gucardev.wallet.infrastructure.exception.helper.ExceptionUtil.buildException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final SendOtpForRegisterUseCase sendOtpForRegisterUseCase;
    private final SendOtpForChangePasswordUseCase sendOtpForChangePasswordUseCase;
    private final ChangePasswordOtpValidateUseCase changePasswordOtpValidateUseCase;
    private final GenerateTokenByRefreshTokenUseCase generateTokenByRefreshTokenUseCase;
    private final GetRolesAndPermissionsUseCase getRolesAndPermissionsUseCase;

    @Operation(
            summary = "login",
            description = "This api allows you to login and retrieve jwt token"
    )
    @RateLimiter(name = "loginRateLimiter")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        return SuccessResponse.builder()
                .body(loginUserUseCase.execute(loginRequest))
                .build();
    }

    @Operation(
            summary = "refresh token",
            description = "This api refreshes jwt token"
    )
    @RateLimiter(name = "loginRateLimiter")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return SuccessResponse.builder()
                .body(generateTokenByRefreshTokenUseCase.execute(refreshTokenRequest))
                .build();
    }

    @Operation(
            summary = "register a new user",
            description = "This api registers a new user and return created user"
    )
    @PostMapping("/register")
    public ResponseEntity<TokenDto> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        registerUserUseCase.execute(userRegisterRequest);
        return SuccessResponse.builder().build();
    }

    @GetMapping("/get-myself")
    public ResponseEntity<UserDto> getMyself() {
        return SuccessResponse.builder()
                .body(getAuthenticatedUserUseCase.execute())
                .build();
    }

    @Operation(
            summary = "Generate OTP for email verification",
            description = "Generates a 6-digit OTP and sends it to the provided email. The OTP is valid for 3 minutes."
    )
    @PostMapping("/generate-register-otp")
    public ResponseEntity<OtpResponse> generateRegisterOtp(@Valid @RequestBody GenerateOtpRequest request) {
        sendOtpForRegisterUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "Generate OTP for change password verification",
            description = "Generates a 6-digit OTP and sends it to the provided email. The OTP is valid for 3 minutes."
    )
    @PostMapping("/generate-change-password-otp")
    public ResponseEntity<OtpResponse> generateChangePasswordOtp(@Valid @RequestBody GenerateOtpRequest request) {
        sendOtpForChangePasswordUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "Validate Register OTP",
            description = "Validates the OTP provided against the one stored for the given email"
    )
    @PostMapping("/validate-register-otp")
    public ResponseEntity<Void> validateRegisterOtp(@Valid @RequestBody ValidateOtpRequest request) {
        activateUserUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "Validate Password Change OTP",
            description = "Validates the OTP provided against the one stored for the given email"
    )
    @PostMapping("/validate-password-change-otp")
    public ResponseEntity<Void> validatePasswordChangeOtp(@Valid @RequestBody ValidateOtpRequest request) {
        changePasswordOtpValidateUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "Change Password",
            description = "Change password for the given email"
    )
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
        return SuccessResponse.builder().build();
    }

    @Operation(
            summary = "List all roles and permissions",
            description = "This API retrieves all roles and their associated permissions"
    )
    @GetMapping("/roles-permissions")
    public ResponseEntity<List<RolePermissionsDto>> getRolesAndPermissions() {
        return SuccessResponse.builder()
                .body(getRolesAndPermissionsUseCase.execute())
                .build();
    }
}
