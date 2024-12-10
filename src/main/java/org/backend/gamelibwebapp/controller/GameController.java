package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.services.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @PostMapping("add")
    public ResponseEntity<Game> addGame(@RequestBody GameAddRequest request) {
        log.info("Adding new game {}", request.title());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.addGame(request));
    }

    @GetMapping("all")
    public ResponseEntity<List<GameDTO>> showAllGames() {
        return ResponseEntity.ok(gameService.getAcceptedGames());
    }

    @GetMapping("top-three")
    public ResponseEntity<List<GameDTO>> showTopThreeGames() {
        return ResponseEntity.ok(gameService.getTopThreeGames());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<GameDTO> updateById(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {
        log.info("Updating game with id {}", id);
        return ResponseEntity.ok(gameService.updateGameById(id, request));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        log.info("Deleting game with id {}", id);
        return ResponseEntity.ok(gameService.deleteGameById(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<GameDTO> getById(@PathVariable("id") Long id) {
        log.info("Getting game with id {}", id);
        return ResponseEntity.ok(gameService.getGame(id));
    }


}
