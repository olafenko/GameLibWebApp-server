package org.backend.gamelibwebapp.mappers;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.services.GameRatingService;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GameDTOMapper implements Function<Game, GameDTO> {

    private final GameRatingService ratingService;

    @Override
    public GameDTO apply(Game game) {
        return GameDTO.builder()
                .title(game.getTitle())
                .producer(game.getProducer())
                .categories(game.getGameCategory())
                .description(game.getDescription())
                .imageUrl(game.getImageUrl())
                .rating(ratingService.getAverageRating(game.getId()))
                .build();
    }
}
