package com.example.auth.service;

import com.example.auth.dto.*;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    public ApiResponse register(RegisterRequest request) {
        // Validate required fields
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return new ApiResponse(false, "Username is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new ApiResponse(false, "Email is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return new ApiResponse(false, "Password must be at least 6 characters");
        }

        // Check for existing user
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse(false, "Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already registered");
        }

        // Create and save user with hashed password
        User user = new User(request.getUsername(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return new ApiResponse(true, "Registration successful! You can now log in.");
    }

    // Authenticate user login
    public ApiResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return new ApiResponse(false, "Username and password are required");
        }

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "Invalid username or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse(false, "Invalid username or password");
        }

        // Return user info (excluding password)
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());

        return new ApiResponse(true, "Login successful", userData);
    }

    // Generate reset token for forgot password
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new ApiResponse(false, "Email is required");
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists or not (security best practice)
            return new ApiResponse(true, "If the email exists, a reset link has been sent.");
        }

        // Generate a simple UUID token
        String token = UUID.randomUUID().toString();
        User user = userOpt.get();
        user.setResetToken(token);
        userRepository.save(user);

        // In a real app, send this token via email.
        // For this demo, return the token in the response.
        return new ApiResponse(true, "Reset token generated. Check your email (or see response for demo).",
                Map.of("resetToken", token));
    }

    // Reset password using token
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        if (request.getToken() == null || request.getToken().trim().isEmpty()) {
            return new ApiResponse(false, "Reset token is required");
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            return new ApiResponse(false, "New password must be at least 6 characters");
        }

        Optional<User> userOpt = userRepository.findByResetToken(request.getToken());
        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "Invalid or expired reset token");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null); // Clear the token after use
        userRepository.save(user);

        return new ApiResponse(true, "Password has been reset successfully. You can now log in.");
    }

    // Get all users (for database details page)
    public ApiResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> userList = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userList.add(userData);
        }

        return new ApiResponse(true, "Users retrieved successfully", userList);
    }
}
