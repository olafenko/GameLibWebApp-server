package org.backend.gamelibwebapp.dto;

import lombok.NonNull;
import org.backend.gamelibwebapp.entities.enums.GameCategory;

import java.util.List;

public record GameAddRequest(@NonNull String title,@NonNull String producer,@NonNull List<GameCategory> gameCategories,@NonNull String imageUrl) {
}
