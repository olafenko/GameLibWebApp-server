package org.backend.gamelibwebapp.dto;

import lombok.Builder;
import org.backend.gamelibwebapp.entities.enums.GameCategory;

import java.util.List;

@Builder
public record GameDTO(String title, String producer, List<GameCategory> categories ,String imageUrl, Double rating,
                      String description) {
}
