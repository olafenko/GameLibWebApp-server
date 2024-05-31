package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.RegistrationRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.AppUserRole;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;


    public ResponseEntity<?> register(RegistrationRequest request){

        if (appUserRepository.existsByEmail(request.email())){
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        if (appUserRepository.existsByUsername(request.username())){
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .appUserRole(AppUserRole.USER)
                .build();

        appUserRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

}
