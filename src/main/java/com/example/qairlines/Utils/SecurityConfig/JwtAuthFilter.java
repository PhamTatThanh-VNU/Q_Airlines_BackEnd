package com.example.qairlines.Utils.SecurityConfig;

import com.example.qairlines.Model.User;
import com.example.qairlines.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        String jwtToken = request.getHeader("Authorization");

        // Check if the token is missing or already authenticated
        if (jwtToken == null || !jwtToken.startsWith("Bearer ") || context.getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = jwtToken.substring(7); // Remove "Bearer " prefix

        try {
            String username = jwtService.extractSubject(jwtToken);

            // Check if username is not null and token is valid
            if (username != null && jwtService.isTokenValid(jwtToken)) {
                User user = (User) userDetailsService.loadUserByUsername(username);
                var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
            } else {
                // Invalid token - Respond with 401
                throw new RuntimeException("Token is invalid or expired");
            }

        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("{\"message\": \"Token is invalid or expired\"}");
            response.getWriter().flush();
            return; // Stop further filter chain execution
        }

        filterChain.doFilter(request, response);
    }
}
