package org.backend.gamelibwebapp.dto;

import lombok.Builder;
import org.backend.gamelibwebapp.entities.GameCategory;

import java.util.List;

@Builder
public record GameResponseObj(String title, String producer, List<GameCategory> categories,String imageUrl,Double rating) {
}
