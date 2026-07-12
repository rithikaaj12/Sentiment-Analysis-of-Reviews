package com.sentimentanalysis.controller;

import com.sentimentanalysis.dto.JwtResponse;
import com.sentimentanalysis.dto.LoginRequest;
import com.sentimentanalysis.dto.RegisterRequest;
import com.sentimentanalysis.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        JwtResponse jwtResponse = authService.register(request);
        Cookie cookie = new Cookie("token", jwtResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (86400000 / 1000));
        response.addCookie(cookie);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        JwtResponse jwtResponse = authService.authenticate(request, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
