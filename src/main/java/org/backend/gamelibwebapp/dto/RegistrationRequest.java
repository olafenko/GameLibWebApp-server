package org.backend.gamelibwebapp.dto;

import lombok.NonNull;

public record RegistrationRequest(@NonNull String username,@NonNull String email,@NonNull String password) {
}
