package com.example.qairlines.Services;

import com.example.qairlines.Model.User;
import com.example.qairlines.Repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class UserSerives {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        // Perform validation, encode password, etc.
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
