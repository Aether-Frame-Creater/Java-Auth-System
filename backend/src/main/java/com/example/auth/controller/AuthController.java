package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        ApiResponse response = authService.register(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        ApiResponse response = authService.login(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ApiResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        ApiResponse response = authService.resetPassword(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        ApiResponse response = authService.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
