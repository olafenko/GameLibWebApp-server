package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.dto.AuthResponse;
import org.backend.gamelibwebapp.dto.LoginRequest;
import org.backend.gamelibwebapp.dto.RegistrationRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.exception.ResourceAlreadyExistsException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService underTestService;

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @Test
    void should_register_new_user() {

        //given
        given(appUserRepository.existsByUsername(anyString())).willReturn(false);
        given(appUserRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("password");
        given(jwtService.generateToken(any(AppUser.class))).willReturn("jwt");

        RegistrationRequest testRequest = new RegistrationRequest("test", "test@gmail.com", "test");

        //when
        AuthResponse response = underTestService.register(testRequest);

        //then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("jwt");
        assertThat(appUserArgumentCaptor.getValue().getPassword()).isEqualTo("password");

    }
    @Test
    void should_not_register_new_user_and_throw_exception_because_email_is_taken() {

        //given
        given(appUserRepository.existsByEmail(anyString())).willReturn(true);
        RegistrationRequest testRequest = new RegistrationRequest("test", "test@gmail.com", "test");

        //when

        //then
        assertThatThrownBy(() -> underTestService.register(testRequest)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Email is already taken!");
        verify(appUserRepository, never()).save(any(AppUser.class));

    }
    @Test
    void should_not_register_new_user_and_throw_exception_because_username_is_taken() {

        //given
        given(appUserRepository.existsByEmail(anyString())).willReturn(false);
        given(appUserRepository.existsByUsername(anyString())).willReturn(true);
        RegistrationRequest testRequest = new RegistrationRequest("test", "test@gmail.com", "test");

        //when

        //then
        assertThatThrownBy(() -> underTestService.register(testRequest)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Username is already taken!");

        verify(appUserRepository, never()).save(any(AppUser.class));

    }

    @Test
    void should_login_user() {

        //given
        LoginRequest testRequest = new LoginRequest("test", "test");
        AppUser testUser = new AppUser();
        testUser.setUsername(testRequest.username());
        given(appUserRepository.findByUsername(testRequest.username())).willReturn(Optional.of(testUser));
        given(jwtService.generateToken(testUser)).willReturn("jwt");

        //when
        AuthResponse response = underTestService.login(testRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("jwt");
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(testRequest.username(), testRequest.password()));

    }
    @Test
    void should_not_login_user_and_throw_exception_because_username_not_found() {

        //given
        LoginRequest testRequest = new LoginRequest("test", "test");

        given(appUserRepository.findByUsername(testRequest.username())).willReturn(Optional.empty());
        //when

        //then
        assertThatThrownBy(() -> underTestService.login(testRequest)).isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

    }
}