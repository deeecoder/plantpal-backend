package com.plantpal.service;

import com.plantpal.config.JwtUtils;
import com.plantpal.dto.AuthResponse;
import com.plantpal.dto.LoginRequest;
import com.plantpal.dto.RegisterRequest;
import com.plantpal.model.User;
import com.plantpal.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

   

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");
        User user = User.builder()
                .name(req.getName()).email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getEmail());
        return AuthResponse.builder()
                .token(token).type("Bearer")
                .userId(user.getId()).name(user.getName()).email(user.getEmail())
                .build();
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtUtils.generateToken(user.getEmail());
        return AuthResponse.builder()
                .token(token).type("Bearer")
                .userId(user.getId()).name(user.getName()).email(user.getEmail())
                .build();
    }

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}