package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public ResponseEntity<List<Game>> showAllGames(){
        List<Game> allGames = gameRepository.findAll();

        return ResponseEntity.ok().body(allGames);
    }

    public ResponseEntity<?> addGame(GameAddRequest gameAddRequest){

        if(gameRepository.existsByTitle(gameAddRequest.title())){
            return ResponseEntity.badRequest().body("This game already exists.");
        }

        Game game = Game.builder()
                .title(gameAddRequest.title())
                .producer(gameAddRequest.producer())
                .gameCategory(gameAddRequest.gameCategories())
                .imageUrl(gameAddRequest.imageUrl())
                .isAccepted(false)
                .build();

        gameRepository.save(game);

        return ResponseEntity.ok().body(game);
    }

    public ResponseEntity<?> updateById(Long id, UpdateRequest updatedGame){

        Optional<Game> gameOpt = gameRepository.findById(id);

        if (gameOpt.isPresent()){
            Game game = gameOpt.get();

            game.setTitle(updatedGame.title());
            game.setProducer(updatedGame.producer());
            game.setGameCategory(updatedGame.gameCategories());
            game.setImageUrl(updatedGame.imageUrl());

            gameRepository.save(game);

            return ResponseEntity.ok("Game updated successfully!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game with id "+id+" not found.");


    }

    public ResponseEntity<?> deleteById(Long id) {

        if(gameRepository.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id" + id + " does not match any game.");
        }

        gameRepository.deleteById(id);

        return ResponseEntity.ok().body("Game deleted successfully!");
    }

    public ResponseEntity<?> getGame(Long id){

        Optional<Game> gameOpt = gameRepository.findById(id);

        if (gameOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id " + id + " does not match any game.");
        }

        return ResponseEntity.ok().body(gameOpt.get());
    }
}
