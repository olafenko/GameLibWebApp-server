package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.RatingRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.entities.GameRating;
import org.backend.gamelibwebapp.exception.CannotPerformActionException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.repositories.GameRatingRepository;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class GameRatingService {

    private final AppUserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameRatingRepository ratingRepository;

    public GameRating rateGame(RatingRequest request) {

        AppUser userById = userRepository.findById(request.userId()).orElseThrow(() -> new ResourceNotFoundException("User with id " + request.userId() + " not found"));
        Game gameById = gameRepository.findById(request.gameId()).orElseThrow(() -> new ResourceNotFoundException("Game with id " + request.gameId() + " not found"));

        if (!gameById.isAccepted()) {
            throw new CannotPerformActionException("Cannot rate not accepted game.");
        }

        if (checkIfRated(request.userId(), request.gameId())) {
            GameRating ratingUpdate = ratingRepository.getUserRating(request.userId(), request.gameId()).get();
            ratingUpdate.setRate(request.value());
            ratingRepository.save(ratingUpdate);
            return ratingUpdate;
        }

        GameRating rating = GameRating.builder()
                .rate(request.value())
                .game(gameById)
                .user(userById)
                .build();

        ratingRepository.save(rating);

        return rating;

    }

    public Double getAverageRating(Long id) {
        double average = ratingRepository.getRatingsByGameId(id).stream()
                .mapToDouble(GameRating::getRate)
                .average()
                .orElse(0d);

        BigDecimal roundedAverage = new BigDecimal(average).setScale(1, RoundingMode.HALF_UP);

        return roundedAverage.doubleValue();
    }


    private boolean checkIfRated(Long userId, Long gameId) {
        return ratingRepository.getUserRating(userId, gameId).isPresent();
    }

}
