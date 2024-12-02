package com.example.qairlines.Controller;

import com.example.qairlines.DTO.AuthUser;
import com.example.qairlines.Model.User;
import com.example.qairlines.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
