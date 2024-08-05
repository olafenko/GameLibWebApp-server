package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;


class GameServiceTest {

    private  GameService underTestGameService;

    @Mock
    private  GameRatingService gameRatingService;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        underTestGameService = new GameService(gameRepository,gameRatingService);
    }

    @Test
    void should_return_list_of_accepted_games() {

        //given
        //when

        underTestGameService.showAcceptedGames();

        //then


    }

    @Test
    void showGamesToAccept() {
    }

    @Test
    void addGame() {
    }

    @Test
    void updateById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getGame() {
    }

    @Test
    void acceptGame() {
    }
}