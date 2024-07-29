package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.LoginRequest;
import org.backend.gamelibwebapp.dto.RegistrationRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.enums.AppUserRole;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> login(LoginRequest request){

        Optional<AppUser> userByUsername = appUserRepository.findByUsername(request.username());

        if (userByUsername.isEmpty()){
            return ResponseEntity.badRequest().body("Username does not match any account!");
        }

        if(!userByUsername.get().getPassword().equals(request.password())){
            return ResponseEntity.badRequest().body("Password does not match username!");
        }

        return ResponseEntity.ok("Login successfully!");

    }


    public ResponseEntity<?> register(RegistrationRequest request){

        if (appUserRepository.existsByEmail(request.email())){
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        if (appUserRepository.existsByUsername(request.username())){
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        AppUser user = AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(encodedPassword)
                .appUserRole(AppUserRole.ROLE_USER)
                .build();

        appUserRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }



}
