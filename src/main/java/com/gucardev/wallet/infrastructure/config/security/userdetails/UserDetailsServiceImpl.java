package com.gucardev.wallet.infrastructure.config.security.userdetails;

import com.gucardev.wallet.domain.user.usecase.GetUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final GetUserByEmailUseCase getUserByEmailUseCase;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(getUserByEmailUseCase.execute(email).orElseThrow(() -> new UsernameNotFoundException(email)));
    }

}
