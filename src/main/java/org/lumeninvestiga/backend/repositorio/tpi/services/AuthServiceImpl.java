package org.lumeninvestiga.backend.repositorio.tpi.services;

import lombok.extern.slf4j.Slf4j;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserLoginRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserRegistrationRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.AuthResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Role;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.InvalidRegisterException;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.UserRepository;
import org.lumeninvestiga.backend.repositorio.tpi.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthResponse> login(UserLoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails user = userRepository.findByUsername(request.username())
                .orElseThrow();
        String token = jwtService.getToken(user);
        log.info("welcome {}", user.getUsername());
        return Optional.of(new AuthResponse(token));
    }

    @Override
    @Transactional
    public Optional<AuthResponse> register(UserRegistrationRequest request) {
        if(userRepository.existsByEmailAddress(request.emailAddress())) {
            throw new InvalidRegisterException("No se pudo registrar la cuenta");
        } else if (userRepository.existsByUsername(request.username())) {
            throw new InvalidRegisterException("No se pudo registrar la cuenta");
        } else {
            User user = new User();
            user.getUserDetail().setName(request.name());
            user.getUserDetail().setLastName(request.lastName());
            user.getUserDetail().setEmailAddress(request.emailAddress());
            user.setUsername(request.username());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.STUDENT);
            userRepository.save(user);
            log.info("user created");
            return Optional.of(new AuthResponse(jwtService.getToken(user)));
        }
    }
}
