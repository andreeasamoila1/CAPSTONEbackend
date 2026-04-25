package com.capstone.identity_service.service.implementations;

import com.capstone.identity_service.model.User;
import com.capstone.identity_service.model.Profile;
import com.capstone.identity_service.payload.AuthResponse;
import com.capstone.identity_service.payload.LoginRequest;
import com.capstone.identity_service.payload.RegisterRequest;
import com.capstone.identity_service.repository.UserRepository;
import com.capstone.identity_service.repository.ProfileRepository;
import com.capstone.identity_service.security.JwtUtils;
import com.capstone.identity_service.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setUserId(user.getId());

        profileRepository.save(profile);

        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());
    }
}