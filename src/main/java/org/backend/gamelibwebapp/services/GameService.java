package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game with id " + id + " not found."));

        game.setTitle(updatedGame.title());
        game.setProducer(updatedGame.producer());
        game.setGameCategory(updatedGame.gameCategories());
        game.setImageUrl(updatedGame.imageUrl());

        gameRepository.save(game);

        return ResponseEntity.ok("Game updated successfully!");
    }


    public ResponseEntity<?> deleteById(Long id) {

        if(gameRepository.findById(id).isEmpty()){
            return ResponseEntity.badRequest().body("Id does not match any game.");
        }

        gameRepository.deleteById(id);

        return ResponseEntity.ok().body("Game deleted successfully!");
    }
}
