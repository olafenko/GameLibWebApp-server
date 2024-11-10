package org.backend.gamelibwebapp.services;

import lombok.RequiredArgsConstructor;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.exception.CannotPerformActionException;
import org.backend.gamelibwebapp.exception.ResourceAlreadyExistsException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.mappers.GameDTOMapper;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameDTOMapper mapper;
    private final String GAME_NOT_FOUND_MESSAGE = "Game with id %s not found";

    public List<GameDTO> getAcceptedGames() {
        return gameRepository.findAll().stream()
                .filter(Game::isAccepted)
                .map(mapper)
                .toList();
    }

    //ONLY FOR ADMIN USAGE
    public List<Game> getGamesToAccept() {
        return gameRepository.findAll().stream()
                .filter(game -> !game.isAccepted())
                .toList();
    }

    public Game addGame(GameAddRequest gameAddRequest) {

        if (gameRepository.existsByTitle(gameAddRequest.title())) {
            throw new ResourceAlreadyExistsException("Game \"" + gameAddRequest.title() + "\" already exists.");
        }

        Game game = Game.builder()
                .title(gameAddRequest.title())
                .producer(gameAddRequest.producer())
                .gameCategory(gameAddRequest.gameCategories())
                .description(gameAddRequest.description())
                .imageUrl(gameAddRequest.imageUrl())
                .isAccepted(false)
                .build();

        gameRepository.save(game);

        return game;
    }


    public GameDTO updateGameById(Long id, UpdateRequest updatedGame) {

        Game gameToUpdate = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));

        gameToUpdate.setTitle(updatedGame.title());
        gameToUpdate.setProducer(updatedGame.producer());
        gameToUpdate.setGameCategory(updatedGame.gameCategories());
        gameToUpdate.setImageUrl(updatedGame.imageUrl());
        gameToUpdate.setDescription(updatedGame.description());
        gameToUpdate.setAccepted(false);

        gameRepository.save(gameToUpdate);

        return mapper.apply(gameToUpdate);
    }

    //ADMIN USAGE
    public String deleteGameById(Long id) {

        Game gameToDelete = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));

        gameRepository.deleteById(id);

        return gameToDelete.getTitle() + " deleted successfully.";
    }


    public GameDTO getGame(Long id) {

        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));

        if (!game.isAccepted()) {
            throw new CannotPerformActionException("Cannot get not accepted game");
        }

        return mapper.apply(game);
    }

    //ADMIN USAGE
    public Game acceptGame(Long id) {
        Game gameById = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(GAME_NOT_FOUND_MESSAGE, id)));
        gameById.setAccepted(true);
        gameRepository.save(gameById);

        return gameById;
    }

    public List<GameDTO> getTopThreeGames() {

        return gameRepository.findAll().stream()
                .filter(Game::isAccepted)
                .map(mapper)
                .sorted(Comparator.comparing(GameDTO::rating).reversed())
                .limit(3)
                .toList();
    }

}
