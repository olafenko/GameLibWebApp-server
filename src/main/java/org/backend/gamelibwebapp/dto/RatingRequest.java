package org.backend.gamelibwebapp.dto;

public record RatingRequest(double value,Long gameId, Long userId) {
}
