package org.backend.gamelibwebapp.dto;

import org.backend.gamelibwebapp.entities.GameCategory;

import java.util.List;

public record UpdateRequest(String title, String producer, List<GameCategory> gameCategories, String imageUrl) {
}
