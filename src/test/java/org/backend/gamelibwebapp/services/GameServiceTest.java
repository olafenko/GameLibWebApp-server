package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private  GameService underTestService;

    @Mock
    private  GameRatingService gameRatingService;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        underTestService = new GameService(gameRepository,gameRatingService);
    }


    @Test
    void should_get_accepted_games() {
        //given
        //when
        underTestService.showAcceptedGames();
        //then
        verify(gameRepository).getAccepted();
    }

    @Test
    void should_get_not_accepted_games() {
        //given
        //when
        underTestService.showGamesToAccept();
        //then
        verify(gameRepository).getNotAccepted();
    }

    @Test
    void should_add_game() {
        //given
        GameAddRequest gameAddRequest = new GameAddRequest("Gothic",null,null,null);
        //when
        underTestService.addGame(gameAddRequest);
        //then
        ArgumentCaptor<Game> gameArgumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game game = gameArgumentCaptor.getValue();
        assertThat(game.getTitle()).isEqualTo(gameAddRequest.title());
        assertThat(game.getProducer()).isEqualTo(gameAddRequest.producer());
        assertThat(game.getGameCategory()).isEqualTo(gameAddRequest.gameCategories());
        assertThat(game.getImageUrl()).isEqualTo(gameAddRequest.imageUrl());
        assertThat(game.isAccepted()).isFalse();
    }

    //TU SKONCZYLEM
    @Test
    void should_not_add_game_because_it_already_exists() {

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