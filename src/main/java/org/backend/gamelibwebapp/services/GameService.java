package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameResponseObj;
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
    private final GameRatingService ratingService;


    //SHOW ALL ACCEPTED GAMES FOR USER USAGE
    public ResponseEntity<List<GameResponseObj>> showAcceptedGames(){
        List<Game> accepted = gameRepository.getAccepted();

        return ResponseEntity.ok(mapAllGamesToResponse(accepted));
    }

    public ResponseEntity<List<Game>> showGamesToAccept(){
        return ResponseEntity.ok(gameRepository.getNotAccepted());
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

    //ADMIN USAGE
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

        return ResponseEntity.ok().body(mapToGameResponse(gameOpt.get()));
    }

    public void acceptGame(Long id){
        Game gameById = gameRepository.findById(id).get();
        gameById.setAccepted(true);
        gameRepository.save(gameById);

    }

    private GameResponseObj mapToGameResponse(Game game){

        return GameResponseObj.builder()
                .title(game.getTitle())
                .producer(game.getProducer())
                .categories(game.getGameCategory())
                .imageUrl(game.getImageUrl())
                .rating(ratingService.getAverageRating(game))
                .build();

    }

    private List<GameResponseObj> mapAllGamesToResponse(List<Game> games){

        return games.stream()
        .map(this::mapToGameResponse)
        .toList();
    }
}
