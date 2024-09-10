package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.entities.GameRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest
class GameRatingRepositoryTest {

    @Autowired
    private  GameRatingRepository underTestRepository;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private AppUserRepository appUserRepository;


    @BeforeEach
    void setUp() {
        underTestRepository.deleteAll();
    }

    @Test
    void should_get_user_rating_by_game_and_user_id_and_return_game_rating_class() {

        //given

        AppUser testUser = new AppUser(1L,null, null, null, null);
        appUserRepository.save(testUser);

        Game testGame = new Game(1L,null, null, null, null, false);
        gameRepository.save(testGame);

        GameRating testRating = new GameRating(null, 0, testUser, testGame);
        underTestRepository.save(testRating);

        //when
        Optional<GameRating> result = underTestRepository.getUserRating(testUser.getId(), testGame.getId());
        //then

        assertThat(result.get()).isEqualTo(testRating);

    }

    @Test
    void should_throw_exception_if_user_rating_not_found() {

        //given
        //when
        //then
        assertThatThrownBy( () -> underTestRepository.getUserRating(0L, 0L).get())
                .isInstanceOf(NoSuchElementException.class);

    }
}