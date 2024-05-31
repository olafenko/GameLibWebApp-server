package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.backend.gamelibwebapp.dto.RegistrationRequest;
import org.backend.gamelibwebapp.services.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping("signup")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        log.info("Registration user: {}", request.username());
        return appUserService.register(request);
    }



}
