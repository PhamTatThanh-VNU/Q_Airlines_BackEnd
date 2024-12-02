package com.example.qairlines.Services;

import com.example.qairlines.DTO.AuthUser;
import com.example.qairlines.Model.Role;
import com.example.qairlines.Model.User;
import com.example.qairlines.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices {
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
        return jwtService.generateJwtToken(authUser.getUsername());
    }

}
