package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.RatingRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.entities.GameRating;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.repositories.GameRatingRepository;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameRatingService {

    private final AppUserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameRatingRepository ratingRepository;

    public ResponseEntity<?> rateGame(RatingRequest request){

        Optional<AppUser> userById = userRepository.findById(request.userId());
        Optional<Game> gameById = gameRepository.findById(request.gameId());

        if(userById.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        if(gameById.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found.");
        }

        GameRating gameRate = GameRating.builder()
                .rate(request.value())
                .game(gameById.get())
                .user(userById.get())
                .build();

        ratingRepository.save(gameRate);

        return ResponseEntity.ok("Game rating added successfully!");

    }



}
