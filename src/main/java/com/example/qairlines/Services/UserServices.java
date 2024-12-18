package com.example.qairlines.Services;

import com.example.qairlines.DTO.AuthUser;
import com.example.qairlines.DTO.GoogleTokenInfo;
import com.example.qairlines.Model.Role;
import com.example.qairlines.Model.User;
import com.example.qairlines.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class UserServices {
    @Value("${jwt.secret}")
    private String secretPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * The function to register new user
     * @param user is user information
     * @return new User
     */
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

    /**
     * The function to login with user use username and password
     * @param authUser include username and password
     * @return jwt token to use other api
     */
    public String auth(AuthUser authUser) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword()));
        User user = userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return jwtService.generateJwtToken(user.getUsername(), user.getRole());
    }

    /**
     * The function to login with google oauth2
     * @param accessToken is access token of client
     * @return jwt token to use other api
     */
    public String googleAuth(String accessToken) {
        GoogleTokenInfo tokenInfo = fetchGoogleTokenInfo(accessToken);

        String email = tokenInfo.getEmail();
        String name = tokenInfo.getName();

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

    /**
     * Utils function to get user information from access Token
     * @param accessToken pass as argument
     * @return data of user
     */
    private GoogleTokenInfo fetchGoogleTokenInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleTokenInfo> response = restTemplate.exchange(
                    googleUserInfoUrl,
                    HttpMethod.GET,
                    requestEntity,
                    GoogleTokenInfo.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new IllegalArgumentException("Invalid Google Access Token");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching Google user info", e);
        }
    }

}
