package org.backend.gamelibwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.gamelibwebapp.dto.RatingRequest;
import org.backend.gamelibwebapp.services.GameRatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final GameRatingService gameRatingService;

    @PostMapping("rate")
    public ResponseEntity<?> rate(@RequestBody RatingRequest request){
        log.info("Rating game with id {}, by user with id {}",request.gameId(),request.userId());
        return gameRatingService.rateGame(request);
    }


}
