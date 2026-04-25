package com.capstone.identity_service.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String id;
    private String email;
    private String username;
    private String role;
}