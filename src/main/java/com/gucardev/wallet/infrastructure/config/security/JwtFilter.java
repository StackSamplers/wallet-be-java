package com.gucardev.wallet.infrastructure.config.security;

import com.gucardev.wallet.infrastructure.config.aspect.annotation.ExcludeFromAspect;
import com.gucardev.wallet.infrastructure.config.security.userdetails.UserDetailsServiceImpl;
import com.gucardev.wallet.infrastructure.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@ExcludeFromAspect
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil tokenService;
    private static final String USERNAME_MDC_KEY = "USERNAME_MDC";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = extractAndDecryptToken(request);

            if (jwt != null && tokenService.validateToken(jwt)) {
                String username = tokenService.extractUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // Add username to MDC
                MDC.put(USERNAME_MDC_KEY, username);
            } else {
                // For unauthenticated requests, set a default value
                MDC.put(USERNAME_MDC_KEY, "anonymous");
            }

        } catch (Exception e) {
            logger.error("Authentication error: ", e);

            // Set anonymous for error cases
            MDC.put(USERNAME_MDC_KEY, "anonymous");

            // Don't send error response for missing tokens, only for invalid ones
            if (!(e instanceof IllegalArgumentException && e.getMessage().contains("No JWT token found"))) {
                sendErrorResponse(response);
                return;
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // IMPORTANT: Clear the MDC after the request is processed
            // This prevents memory leaks in application servers
            MDC.remove(USERNAME_MDC_KEY);
        }
    }

    private String extractAndDecryptToken(HttpServletRequest request) throws Exception {
        // First check for Authorization header
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            // For regular HTTP requests
            return header.substring(7);
        }

        // For WebSocket connections where we can't set custom headers
        String encryptedJwt = request.getParameter("token");
        if (encryptedJwt != null && !encryptedJwt.isEmpty()) {
            return encryptedJwt;
        }

        // No token found in either location
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}


