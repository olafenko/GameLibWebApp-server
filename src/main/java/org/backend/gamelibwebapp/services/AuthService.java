package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.AuthResponse;
import org.backend.gamelibwebapp.dto.LoginRequest;
import org.backend.gamelibwebapp.dto.RegistrationRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.enums.AppUserRole;
import org.backend.gamelibwebapp.exception.ResourceAlreadyExistsException;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public String register(RegistrationRequest request) {

        if (appUserRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email is already taken!");
        }

        if (appUserRepository.existsByUsername(request.username())) {
            throw new ResourceAlreadyExistsException("Username is already taken!");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .appUserRole(AppUserRole.ROLE_USER)
                .build();

        appUserRepository.save(user);


        return "Registered successfully! Now u can log in by using your credentials.";
    }

    public AuthResponse login(LoginRequest loginRequest) {

        authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        AppUser user = appUserRepository.findByUsername(loginRequest.username()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwt = jwtService.generateToken(user);

        return new AuthResponse(jwt);

    }


}
