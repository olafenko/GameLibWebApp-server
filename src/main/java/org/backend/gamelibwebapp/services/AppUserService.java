package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.LoginRequest;
import org.backend.gamelibwebapp.dto.RegistrationRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.AppUserRole;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public ResponseEntity<?> login(LoginRequest request){

        if (!appUserRepository.existsByUsername(request.username())){
            return ResponseEntity.badRequest().body("Username does not match any account!");
        }

        AppUser userByUsername = appUserRepository.findByUsername(request.username());

        if(!userByUsername.getPassword().equals(request.password())){
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username);
    }


}
