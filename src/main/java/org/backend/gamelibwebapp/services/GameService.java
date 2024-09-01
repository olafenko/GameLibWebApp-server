package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.exception.CannotPerformActionException;
import org.backend.gamelibwebapp.exception.ResourceAlreadyExistsException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameRatingService ratingService;
    private final String GAME_NOT_FOUND_MESSAGE = "Game with id %s not found";

    //SHOW ALL ACCEPTED GAMES FOR USER USAGE
    public List<GameDTO> showAcceptedGames() {
        List<Game> accepted = gameRepository.getAccepted();
        return mapAllGamesToResponse(accepted);
    }

    public List<Game> showGamesToAccept() {
        return gameRepository.getNotAccepted();
    }

    public Game addGame(GameAddRequest gameAddRequest) {

        if (gameRepository.existsByTitle(gameAddRequest.title())) {
            throw new ResourceAlreadyExistsException("Game \"" + gameAddRequest.title() + "\" already exists.");
        }

        Game game = Game.builder()
                .title(gameAddRequest.title())
                .producer(gameAddRequest.producer())
                .gameCategory(gameAddRequest.gameCategories())
                .imageUrl(gameAddRequest.imageUrl())
                .isAccepted(false)
                .build();

        gameRepository.save(game);

        return game;
    }


    public GameDTO updateById(Long id, UpdateRequest updatedGame) {

        Game gameToUpdate = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));

        gameToUpdate.setTitle(updatedGame.title());
        gameToUpdate.setProducer(updatedGame.producer());
        gameToUpdate.setGameCategory(updatedGame.gameCategories());
        gameToUpdate.setImageUrl(updatedGame.imageUrl());

        gameRepository.save(gameToUpdate);

        return mapToGameResponse(gameToUpdate);
    }

    //ADMIN USAGE
    public String deleteById(Long id) {

        Game gameToDelete = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));

        gameRepository.deleteById(id);

        return gameToDelete.getTitle() + " deleted successfully.";
    }


    public GameDTO getGame(Long id) {

        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));

        if (!game.isAccepted()) {
            throw new CannotPerformActionException("Cannot get not accepted game");
        }

        return mapToGameResponse(game);
    }

    public Game acceptGame(Long id) {
        Game gameById = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));
        gameById.setAccepted(true);
        gameRepository.save(gameById);

        return gameById;
    }

    public List<GameDTO> topThreeGames() {

        List<Game> acceptedGames = gameRepository.getAccepted();
        List<GameDTO> allGames = mapAllGamesToResponse(acceptedGames);

        return allGames.stream()
                .sorted(Comparator.comparing(GameDTO::rating))
                .limit(3)
                .toList();

    }

    private GameDTO mapToGameResponse(Game game) {

        return GameDTO.builder()
                .title(game.getTitle())
                .producer(game.getProducer())
                .categories(game.getGameCategory())
                .imageUrl(game.getImageUrl())
                .rating(ratingService.getAverageRating(game))
                .build();

    }

    private List<GameDTO> mapAllGamesToResponse(List<Game> games) {

        return games.stream()
                .map(this::mapToGameResponse)
                .toList();
    }
}
