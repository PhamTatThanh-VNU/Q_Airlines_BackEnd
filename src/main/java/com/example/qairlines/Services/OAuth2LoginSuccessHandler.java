package com.example.qairlines.Services;

import com.example.qairlines.Model.Role;
import com.example.qairlines.Model.User;
import com.example.qairlines.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2Token.getPrincipal();

        String username = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setFullName(fullName);
        newUser.setRole(Role.USER.name());

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            userRepository.save(newUser);
        } else {
            newUser = existingUser.get();
        }

        String jwtToken = jwtService.generateJwtToken(newUser.getUsername());
        response.setHeader("Authorization", "Bearer " + jwtToken);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
