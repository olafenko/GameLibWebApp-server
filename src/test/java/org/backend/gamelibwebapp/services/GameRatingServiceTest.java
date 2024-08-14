package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.dto.RatingRequest;
import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.entities.GameRating;
import org.backend.gamelibwebapp.exception.CannotPerformActionException;
import org.backend.gamelibwebapp.exception.ResourceNotFoundException;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.repositories.GameRatingRepository;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class GameRatingServiceTest {


    private GameRatingService underTestService;

    @Mock
    private GameRatingRepository gameRatingRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private AppUserRepository appUserRepository;

    private final Long sampleId = 1L;


    @BeforeEach
    void setUp() {
        underTestService = Mockito.spy(new GameRatingService(appUserRepository, gameRepository, gameRatingRepository));
    }


    @Test
    void should_add_new_game_rating() {
        //given
        Game game = Game.builder().id(sampleId).title("Gothic").isAccepted(true).ratings(new ArrayList<>()).build();
        AppUser user = AppUser.builder().id(sampleId).username("user").email("email").password("user").build();

        RatingRequest testRatingRequest = new RatingRequest(4, sampleId, sampleId);

        given(appUserRepository.findById(sampleId)).willReturn(Optional.of(user));
        given(gameRepository.findById(sampleId)).willReturn(Optional.of(game));

        //when
        GameRating gameRatingResult = underTestService.rateGame(testRatingRequest);

        //then
        assertThat(gameRatingResult.getRate()).isEqualTo(testRatingRequest.value());
        assertThat(gameRatingResult.getGame()).isEqualTo(game);
        assertThat(gameRatingResult.getUser()).isEqualTo(user);
        assertThat(game.getRatings().contains(gameRatingResult)).isTrue();
        verify(gameRatingRepository).save(gameRatingResult);

    }

    @Test
    void should_update_existing_game_rating() {
        //given
        Game game = Game.builder().id(sampleId).title("Gothic").isAccepted(true).ratings(new ArrayList<>()).build();
        AppUser user = AppUser.builder().id(sampleId).username("user").email("email").password("user").build();
        GameRating existingGameRating = GameRating.builder().rate(2).game(game).user(user).build();

        RatingRequest testRatingRequest = new RatingRequest(4, sampleId, sampleId);

        given(appUserRepository.findById(sampleId)).willReturn(Optional.of(user));
        given(gameRepository.findById(sampleId)).willReturn(Optional.of(game));

        given(gameRatingRepository.getUserRating(sampleId,sampleId)).willReturn(Optional.of(existingGameRating));

        //when
        GameRating updatedGameRating = underTestService.rateGame(testRatingRequest);

        //then
        assertThat(updatedGameRating.getRate()).isEqualTo(testRatingRequest.value());
        verify(gameRatingRepository).save(updatedGameRating);

    }
    @Test
    void should_not_add_game_rating_and_throw_exception_because_user_does_not_exist() {
        //given
        RatingRequest testRatingRequest = new RatingRequest(4, sampleId, sampleId);
        given(appUserRepository.findById(sampleId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTestService.rateGame(testRatingRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id " + testRatingRequest.userId() + " not found");
    }
    @Test
    void should_not_add_game_rating_and_throw_exception_because_game_does_not_exist() {
        //given
        RatingRequest testRatingRequest = new RatingRequest(4, sampleId, sampleId);
        given(appUserRepository.findById(sampleId)).willReturn(Optional.of(new AppUser()));
        given(gameRepository.findById(sampleId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTestService.rateGame(testRatingRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Game with id " + testRatingRequest.userId() + " not found");
    }
    @Test
    void should_not_add_game_rating_and_throw_exception_because_game_is_not_accepted() {
        //given
        RatingRequest testRatingRequest = new RatingRequest(4, sampleId, sampleId);
        Game game = Game.builder().id(sampleId).title("Gothic").isAccepted(false).build();

        given(appUserRepository.findById(sampleId)).willReturn(Optional.of(new AppUser()));
        given(gameRepository.findById(sampleId)).willReturn(Optional.of(game));

        //when
        //then
        assertThatThrownBy(() -> underTestService.rateGame(testRatingRequest))
                .isInstanceOf(CannotPerformActionException.class)
                .hasMessageContaining("Cannot rate not accepted game.");
    }

    @Test
    void should_get_average_rating() {

        //given
        GameRating rating1 = new GameRating();
        GameRating rating2 = new GameRating();

        rating1.setRate(3);
        rating2.setRate(5);

        Game game = new Game();
        game.setRatings(List.of(rating1, rating2));
        //when
        double result = underTestService.getAverageRating(game);

        //then
        assertThat(result).isEqualTo(4);

    }
    @Test
    void should_get_average_rating_and_return_0() {

        //given
        Game game = new Game();
        game.setRatings(List.of());
        //when
        double result = underTestService.getAverageRating(game);

        //then
        assertThat(result).isEqualTo(0);

    }
    @Test
    void should_get_average_rating_and_round_it_correctly() {

        //given
        GameRating rating1 = new GameRating();
        GameRating rating2 = new GameRating();

        rating1.setRate(2.25);
        rating2.setRate(4.4);

        Game game = new Game();
        game.setRatings(List.of(rating1, rating2));
        //when
        double result = underTestService.getAverageRating(game);

        //then
        assertThat(result).isEqualTo(3.3);

    }

}