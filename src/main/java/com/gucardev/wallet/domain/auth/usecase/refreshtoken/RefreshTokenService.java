package com.gucardev.wallet.domain.auth.usecase.refreshtoken;

import com.gucardev.wallet.domain.auth.entity.RefreshToken;
import com.gucardev.wallet.domain.user.entity.User;

public interface RefreshTokenService {
    String generateAndSaveRefreshToken(User user);

    RefreshToken findByToken(String token);

    boolean isTokenValid(RefreshToken token);
}
