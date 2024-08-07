package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameResponseObj;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/game")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @PostMapping("add")
    public ResponseEntity<Game> addGame(@RequestBody GameAddRequest request){
        log.info("Adding new game {}",request.title());
        return ResponseEntity.ok(gameService.addGame(request));
    }

    @GetMapping("all")
    public ResponseEntity<List<GameResponseObj>> showAllGames(){
        return ResponseEntity.ok(gameService.showAcceptedGames());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<GameResponseObj> updateById(@PathVariable("id") Long id,@RequestBody UpdateRequest request){
        log.info("Updating game with id {}",id);
        return ResponseEntity.ok(gameService.updateById(id,request));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteById (@PathVariable("id") Long id){
        log.info("Deleting game with id {}",id);
        return ResponseEntity.ok(gameService.deleteById(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<GameResponseObj> getById(@PathVariable("id") Long id){
        log.info("Getting game with id {}",id);
        return ResponseEntity.ok(gameService.getGame(id));
    }

}
