package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/game")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @PostMapping("add")
    public ResponseEntity<?> addGame(@RequestBody GameAddRequest request){
        log.info("Adding new game {}",request.title());
        return gameService.addGame(request);
    }

    @GetMapping("all")
    public ResponseEntity<?> showAllGames(){
        return gameService.showAllGames();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteById (@PathVariable("id") Long id){

        log.info("Deleting game with id {}",id);
        return gameService.deleteById(id);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Long id,@RequestBody UpdateRequest request){
        log.info("Updating game with id {}",id);
        return gameService.updateById(id,request);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        log.info("Getting game with id {}",id);
        return gameService.getGame(id);
    }






}
