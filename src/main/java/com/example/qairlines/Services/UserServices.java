package com.example.qairlines.Services;

import com.example.qairlines.DTO.AuthUser;
import com.example.qairlines.Model.Role;
import com.example.qairlines.Model.User;
import com.example.qairlines.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices {
    @Value("${jwt.secret}")
    private String secretPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }
        User regUser = User.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.USER.name())
                .build();
        return userRepository.save(regUser);
    }

    public String auth(AuthUser authUser) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword()));
        User user = userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return jwtService.generateJwtToken(user.getUsername(), user.getRole());
    }
    public String googleAuth(String googleToken, JwtDecoder jwtDecoder) {
        Jwt decodedToken = jwtDecoder.decode(googleToken);

        String email = decodedToken.getClaimAsString("email");
        String name = decodedToken.getClaimAsString("name");

        User user = userRepository.findByUsername(email).orElseGet(() -> {
            User newUser = User.builder()
                    .username(email)
                    .fullName(name)
                    .password(passwordEncoder.encode(secretPassword))
                    .role(Role.USER.name())
                    .build();
            return userRepository.save(newUser);
        });

        return jwtService.generateJwtToken(user.getUsername(), user.getRole());
    }

}
