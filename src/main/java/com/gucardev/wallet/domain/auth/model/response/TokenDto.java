package com.gucardev.wallet.domain.auth.model.response;

import com.gucardev.wallet.domain.user.model.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private UserDto user;
}
