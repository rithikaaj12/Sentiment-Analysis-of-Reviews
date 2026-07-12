package com.sentimentanalysis.service;

import com.sentimentanalysis.dto.JwtResponse;
import com.sentimentanalysis.dto.LoginRequest;
import com.sentimentanalysis.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtResponse register(RegisterRequest request);
    JwtResponse authenticate(LoginRequest loginRequest, HttpServletResponse response);
}
