package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final GameService gameService;

    @GetMapping("to-accept")
    public ResponseEntity<List<Game>> getGamesToAccept(){
        return ResponseEntity.ok(gameService.showGamesToAccept());
    }

    @PostMapping("accept/{id}")
    public ResponseEntity<Game> acceptGame(@PathVariable("id") Long id){
        return ResponseEntity.ok(gameService.acceptGame(id));
    }

    @DeleteMapping("reject/{id}")
    public ResponseEntity<String> rejectGame (@PathVariable("id") Long id){
        log.info("Rejecting game with id {}",id);
        return ResponseEntity.ok(gameService.deleteById(id));
    }

}
