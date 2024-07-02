package org.backend.gamelibwebapp.dto;

public record RatingRequest(int value,Long gameId, Long userId) {
}
