package com.example.webtmdt.service;

import com.example.webtmdt.dto.request.LoginRequest;
import com.example.webtmdt.dto.request.RegisterRequest;
import com.example.webtmdt.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
