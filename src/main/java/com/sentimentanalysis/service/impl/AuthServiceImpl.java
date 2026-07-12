package com.sentimentanalysis.service.impl;

import com.sentimentanalysis.dto.JwtResponse;
import com.sentimentanalysis.dto.LoginRequest;
import com.sentimentanalysis.dto.RegisterRequest;
import com.sentimentanalysis.entity.User;
import com.sentimentanalysis.exception.BadRequestException;
import com.sentimentanalysis.jwt.JwtUtils;
import com.sentimentanalysis.repository.UserRepository;
import com.sentimentanalysis.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = new User(
            request.getFullName().trim(),
            request.getUsername().trim(),
            request.getEmail().trim(),
            passwordEncoder.encode(request.getPassword()),
            "ROLE_USER"
        );
        user.setEnabled(true);
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        return new JwtResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public JwtResponse authenticate(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByUsername(loginRequest.getUsername().trim())
            .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtUtils.getExpirationMs() / 1000));
        response.addCookie(cookie);

        return new JwtResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
