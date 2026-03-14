package com.ticketbooking.controller;

import com.ticketbooking.model.User;
import com.ticketbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username").toLowerCase();
        String password = body.get("password");
        String email = body.getOrDefault("email", username + "@cinewave.com");
        String name = body.getOrDefault("name", body.get("username"));

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already taken"));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In production, hash this!
        user.setEmail(email);
        user.setRole(User.Role.USER);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Registration successful", "name", name, "username", username, "role", "user"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username").toLowerCase();
        String password = body.get("password");

        // Admin hardcoded check
        if ("admin".equals(username) && "admin123".equals(password)) {
            return ResponseEntity.ok(Map.of("message", "Login successful", "name", "Administrator", "username", "admin", "role", "admin"));
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Incorrect password"));
        }

        return ResponseEntity.ok(Map.of("message", "Login successful", "name", user.getUsername(), "username", user.getUsername(), "role", user.getRole().toString().toLowerCase()));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
