package org.lumeninvestiga.backend.repositorio.tpi.controllers;

import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserLoginRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserRegistrationRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.AuthResponse;
import org.lumeninvestiga.backend.repositorio.tpi.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        Optional<AuthResponse> response = authService.login(request);
        return response.map(authResponse -> ResponseEntity.status(HttpStatus.OK).body(authResponse))
                .orElseGet(
                        () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
                );
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRegistrationRequest request) {
        Optional<AuthResponse> response = authService.register(request);
        return response.map(authResponse -> ResponseEntity.status(HttpStatus.OK).body(authResponse))
                .orElseGet(
                        () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
                );
    }
}
