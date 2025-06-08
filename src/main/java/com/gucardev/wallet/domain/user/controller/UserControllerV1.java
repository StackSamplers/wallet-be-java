package com.gucardev.wallet.domain.user.controller;


import com.gucardev.wallet.domain.user.mapper.UserMapper;
import com.gucardev.wallet.domain.user.model.request.UserCreateRequest;
import com.gucardev.wallet.domain.user.model.request.UserFilterRequest;
import com.gucardev.wallet.domain.user.model.request.UserUpdateRequest;
import com.gucardev.wallet.domain.user.model.request.UserUpdateUseCaseParam;
import com.gucardev.wallet.domain.user.model.response.UserDto;
import com.gucardev.wallet.domain.user.usecase.CreateUserUseCase;
import com.gucardev.wallet.domain.user.usecase.GetUserByIdUseCase;
import com.gucardev.wallet.domain.user.usecase.SearchUsersUseCase;
import com.gucardev.wallet.domain.user.usecase.UpdateUserUseCase;
import com.gucardev.wallet.infrastructure.response.ApiResponse;
import com.gucardev.wallet.infrastructure.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Controller for user operations")
public class UserControllerV1 {

    private final CreateUserUseCase createUserUseCase;
    private final SearchUsersUseCase searchUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UserMapper userMapper;

    @Operation(
            summary = "Create a new user",
            description = "This api creates a new user and return created user"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return ApiResponse.success().body(createUserUseCase.execute(userCreateRequest)).build();
    }

    @Operation(
            summary = "Search users",
            description = "This api allows you to search and filter users with pagination"
    )
    @GetMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(@Valid @ParameterObject UserFilterRequest filterRequest) {
        return SuccessResponse.builder().body(searchUsersUseCase.execute(filterRequest)).build();
    }

    @Operation(
            summary = "Get user by id",
            description = "This api retrieves user by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@Valid @NotNull @PathVariable Long id) {
        var result = getUserByIdUseCase.executeAndThrowExceptionIfNotFound(id);
        return SuccessResponse.builder().body(userMapper.toDto(result)).build();
    }

    @Operation(
            summary = "Update existing user",
            description = "This api updates user"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @PathVariable Long id) {
        return SuccessResponse.builder().body(updateUserUseCase.execute(new UserUpdateUseCaseParam(id, userUpdateRequest))).build();
    }

}
