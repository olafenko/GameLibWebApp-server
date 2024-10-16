package org.backend.gamelibwebapp.dto;

import lombok.NonNull;

public record RatingRequest(@NonNull Double value, @NonNull Long gameId,@NonNull Long userId) {
}
