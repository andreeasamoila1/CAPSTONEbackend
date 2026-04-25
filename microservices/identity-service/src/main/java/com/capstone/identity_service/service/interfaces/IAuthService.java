package com.capstone.identity_service.service.interfaces;

import com.capstone.identity_service.payload.AuthResponse;
import com.capstone.identity_service.payload.LoginRequest;
import com.capstone.identity_service.payload.RegisterRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}