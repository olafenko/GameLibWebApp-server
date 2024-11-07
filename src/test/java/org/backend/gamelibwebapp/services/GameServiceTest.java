package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.dto.GameAddRequest;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.dto.UpdateRequest;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.exception.CannotPerformActionException;
import org.backend.gamelibwebapp.exception.ResourceAlreadyExistsException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.mappers.GameDTOMapper;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameRatingService gameRatingService;

    private GameService underTestService;
    private GameDTOMapper mapper;
    private final Long sampleId = 1L;

    @BeforeEach
    void setUp() {
        mapper = new GameDTOMapper(gameRatingService);
        underTestService = new GameService(gameRepository, mapper);
    }

    @Test
    void should_get_accepted_games() {

        //given
        Game acceptedGame = mock(Game.class);
        Game notAcceptedGame = mock(Game.class);

        when(acceptedGame.isAccepted()).thenReturn(true);
        when(acceptedGame.getId()).thenReturn(1L);

        when(notAcceptedGame.isAccepted()).thenReturn(false);

        when(gameRepository.findAll()).thenReturn(List.of(acceptedGame, notAcceptedGame));

        when(gameRatingService.getAverageRating(1L)).thenReturn(4.5);

        //when
        List<GameDTO> result = underTestService.getAcceptedGames();
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).rating()).isEqualTo(4.5);

        verify(gameRatingService).getAverageRating(1L);

    }

    @Test
    void should_get_not_accepted_games() {

        //given
        Game acceptedGame = mock(Game.class);
        Game notAcceptedGame = mock(Game.class);

        when(acceptedGame.isAccepted()).thenReturn(true);
        when(notAcceptedGame.isAccepted()).thenReturn(false);

        when(gameRepository.findAll()).thenReturn(List.of(acceptedGame, notAcceptedGame));

        //when
        List<Game> result = underTestService.getGamesToAccept();
        //then
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void should_add_game() {

        //given
        GameAddRequest gameAddRequest = new GameAddRequest("Gothic", "", List.of(), "null");

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
        GameAddRequest gameAddRequest = new GameAddRequest("Gothic", "", List.of(), "null");
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
                .gameCategory(List.of())
                .imageUrl("")
                .producer("")
                .isAccepted(true)
                .build();
        UpdateRequest updateRequest = new UpdateRequest("Gothic 2", "Piranha Bytes", List.of(), "image url");
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

        UpdateRequest updateRequest = new UpdateRequest("Gothic 2", "Piranha Bytes", List.of(), "image url");
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
                .gameCategory(List.of())
                .imageUrl("")
                .producer("")
                .isAccepted(true)
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
                .gameCategory(List.of())
                .imageUrl("")
                .producer("")
                .isAccepted(true)
                .build();

        given(gameRepository.findById(game.getId())).willReturn(Optional.of(game));

        GameDTO expected = mapper.apply(game);

        //when
        GameDTO response = underTestService.getGame(game.getId());

        //then
        assertThat(response).isEqualTo(expected);

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
                .gameCategory(List.of())
                .imageUrl("")
                .producer("")
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
                .gameCategory(List.of())
                .imageUrl("")
                .producer("")
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
        assertThat(capturedGame).isEqualTo(gameToAccept);

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

    @Test
    void should_get_top_three_games_by_average_rating() {

        //given
        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);
        Game game3 = mock(Game.class);
        Game game4 = mock(Game.class);

        when(game1.getId()).thenReturn(1L);
        when(game1.isAccepted()).thenReturn(true);
        when(game2.getId()).thenReturn(2L);
        when(game2.isAccepted()).thenReturn(true);
        when(game3.getId()).thenReturn(3L);
        when(game3.isAccepted()).thenReturn(true);
        when(game4.getId()).thenReturn(4L);
        when(game4.isAccepted()).thenReturn(true);

        when(gameRatingService.getAverageRating(1L)).thenReturn(4.5);
        when(gameRatingService.getAverageRating(2L)).thenReturn(1.5);
        when(gameRatingService.getAverageRating(3L)).thenReturn(5.0);
        when(gameRatingService.getAverageRating(4L)).thenReturn(3.5);

        when(gameRepository.findAll()).thenReturn(List.of(game1, game2, game3, game4));

        //when

        List<GameDTO> result = underTestService.getTopThreeGames();

        //then

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).rating()).isEqualTo(5.0);
        assertThat(result.get(1).rating()).isEqualTo(4.5);
        assertThat(result.get(2).rating()).isEqualTo(3.5);

    }


}