package com.example.qairlines.Controller;

import com.example.qairlines.DTO.AuthUser;
import com.example.qairlines.Model.User;
import com.example.qairlines.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User regUser) {
        return ResponseEntity.ok(userServices.registerUser(regUser));
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody AuthUser authUser) {
        return ResponseEntity.ok(userServices.auth(authUser));
    }
    @PostMapping("/auth/google")
    public ResponseEntity<?> googleLogin(@RequestBody String accessToken) {
        try {
            String jwtToken = userServices.googleAuth(accessToken);
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }
    @GetMapping("/userInfo")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }

}
