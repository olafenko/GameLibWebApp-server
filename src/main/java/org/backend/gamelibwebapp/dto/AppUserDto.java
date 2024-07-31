package org.backend.gamelibwebapp.dto;

import org.backend.gamelibwebapp.entities.enums.AppUserRole;

public record AppUserDto(String username, String email, AppUserRole role) {
}
