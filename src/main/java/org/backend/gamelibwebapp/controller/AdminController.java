package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.gamelibwebapp.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final GameService gameService;



    @GetMapping("all")
    public ResponseEntity<?> showAllGames(){
        return gameService.showAllGames();
    }

    @GetMapping("to-accept")
    public ResponseEntity<?> showNotAcceptedGames(){
        return gameService.showGamesToAccept();
    }

    @PostMapping("accept/{id}")
    public void acceptGame(@PathVariable("id") Long id){
        gameService.acceptGame(id);
    }

    @DeleteMapping("reject/{id}")
    public void rejectGame (@PathVariable("id") Long id){
        log.info("Rejecting game with id {}",id);
        gameService.deleteById(id);
    }

}
