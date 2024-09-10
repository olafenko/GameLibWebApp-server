package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.exception.CannotPerformActionException;
import org.backend.gamelibwebapp.exception.ResourceAlreadyExistsException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private GameService underTestService;

    @Mock
    private GameRatingService gameRatingService;
    @Mock
    private GameRepository gameRepository;

    private final Long sampleId = 1L;


    @BeforeEach
    void setUp() {
        underTestService = new GameService(gameRepository, gameRatingService);
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
        GameAddRequest gameAddRequest = new GameAddRequest("Gothic", null, null, null);

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

    @Test
    void should_not_add_game_and_throw_exception_because_it_already_exists() {

        //given
        GameAddRequest gameAddRequest = new GameAddRequest("Gothic", null, null, null);
        given(gameRepository.existsByTitle(gameAddRequest.title())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTestService.addGame(gameAddRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Game \"" + gameAddRequest.title() + "\" already exists.");

        verify(gameRepository, never()).save(any());

    }

    @Test
    void should_update_game() {

        //given
        Game game = Game.builder()
                .title("Gothic")
                .gameCategory(null)
                .imageUrl(null)
                .producer(null)
                .isAccepted(false)
                .build();
        UpdateRequest updateRequest = new UpdateRequest("Gothic 2", "Piranha Bytes", null, "image url");
        given(gameRepository.findById(game.getId())).willReturn(Optional.of(game));

        //when
        underTestService.updateGameById(game.getId(), updateRequest);

        //then
        ArgumentCaptor<Game> gameArgumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game capturedGame = gameArgumentCaptor.getValue();
        assertThat(capturedGame.getTitle()).isEqualTo(updateRequest.title());
        assertThat(capturedGame.getProducer()).isEqualTo(updateRequest.producer());
        assertThat(capturedGame.getGameCategory()).isEqualTo(updateRequest.gameCategories());
        assertThat(capturedGame.getImageUrl()).isEqualTo(updateRequest.imageUrl());
    }

    @Test
    void should_not_update_game_and_throw_exception_because_it_not_exist() {

        //given

        UpdateRequest updateRequest = new UpdateRequest("Gothic 2", "Piranha Bytes", null, "image url");
        given(gameRepository.findById(sampleId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTestService.updateGameById(sampleId, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Game with id %s not found", sampleId));
    }

    @Test
    void should_delete_game_by_id() {

        //given
        Game gameToDelete = Game.builder()
                .title("Gothic")
                .gameCategory(null)
                .imageUrl(null)
                .producer(null)
                .isAccepted(false)
                .build();
        given(gameRepository.findById(gameToDelete.getId())).willReturn(Optional.of(gameToDelete));

        //when
        String resultMessage = underTestService.deleteGameById(gameToDelete.getId());

        //then
        verify(gameRepository).deleteById(gameToDelete.getId());


        assertThat(resultMessage).isEqualTo(gameToDelete.getTitle() + " deleted successfully.");

    }

    @Test
    void should_not_delete_game_and_throw_exception_because_it_not_exist() {

        //given
        given(gameRepository.findById(any())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTestService.deleteGameById(sampleId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Game with id %s not found", sampleId));

        verify(gameRepository, never()).deleteById(any());

    }

    @Test
    void should_get_response_obj_of_game_by_id() {

        //given
        Game game = Game.builder()
                .title("Gothic")
                .gameCategory(null)
                .imageUrl(null)
                .producer(null)
                .isAccepted(true)
                .build();

        given(gameRatingService.getAverageRating(game.getId())).willReturn(2.5);
        given(gameRepository.findById(game.getId())).willReturn(Optional.of(game));

        //when
        GameDTO response = underTestService.getGame(game.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo(game.getTitle());
        assertThat(response.producer()).isEqualTo(game.getProducer());
        assertThat(response.categories()).isEqualTo(game.getGameCategory());
        assertThat(response.imageUrl()).isEqualTo(game.getImageUrl());
        assertThat(response.rating()).isEqualTo(2.5);

    }

    @Test
    void should_not_get_game_and_throw_exception_because_it_not_exist() {

        //given
        given(gameRepository.findById(any())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTestService.getGame(sampleId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Game with id %s not found", sampleId));

    }

    @Test
    void should_not_get_game_and_throw_exception_because_its_not_accepted() {

        //given
        Game game = Game.builder()
                .title("Gothic")
                .gameCategory(null)
                .imageUrl(null)
                .producer(null)
                .isAccepted(false)
                .build();
        given(gameRepository.findById(any())).willReturn(Optional.of(game));

        //when
        //then
        assertThatThrownBy(() -> underTestService.getGame(game.getId()))
                .isInstanceOf(CannotPerformActionException.class)
                .hasMessageContaining("Cannot get not accepted game");

    }


    @Test
    void should_accept_game() {

        //given
        Game gameToAccept = Game.builder()
                .title("Gothic")
                .gameCategory(null)
                .imageUrl(null)
                .producer(null)
                .isAccepted(false)
                .build();
        given(gameRepository.findById(gameToAccept.getId())).willReturn(Optional.of(gameToAccept));
        //when
        underTestService.acceptGame(gameToAccept.getId());
        //then
        ArgumentCaptor<Game> gameArgumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game capturedGame = gameArgumentCaptor.getValue();
        assertThat(capturedGame.isAccepted()).isTrue();

    }

    @Test
    void should_not_accept_game_and_throw_exception_because_it_not_exist() {

        //given
        given(gameRepository.findById(any())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTestService.acceptGame(sampleId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Game with id %s not found", sampleId));

    }
}