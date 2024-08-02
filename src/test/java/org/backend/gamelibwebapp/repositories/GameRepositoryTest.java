package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class GameRepositoryTest {

    @Autowired
    private GameRepository underTestGameRepository;

    @AfterEach
    void tearDown() {
        underTestGameRepository.deleteAll();
    }

    @Test
    void it_should_check_if_game_exists_by_title_and_return_true() {

        //given
        String title = "Gothic";
        Game game = new Game(null,"Gothic",null,null,null,null,true);
        underTestGameRepository.save(game);
        //when
        boolean result = underTestGameRepository.existsByTitle(title);
        //then
        assertThat(result).isTrue();
    }
    @Test
    void it_should_check_if_game_exists_by_title_and_return_false() {

        //given
        String title = "Gothic";
        //when
        boolean result = underTestGameRepository.existsByTitle(title);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void should_get_accepted_games_and_return_them_from_prepared_list() {

        //given
        List<Game> games = gamesToTest();
        underTestGameRepository.saveAll(games);
        //when
        List<Game> result = underTestGameRepository.getAccepted();
        //then
        List<Game> expected = games.stream().filter(Game::isAccepted).toList();
        assertThat(result).isEqualTo(expected);
    }
    @Test
    void should_get_accepted_games_and_return_empty_list() {

        //given

        //when
        List<Game> result = underTestGameRepository.getAccepted();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void should_get_not_accepted_games_and_return_them_from_prepared_list() {

        //given
        List<Game> games = gamesToTest();
        underTestGameRepository.saveAll(games);
        //when
        List<Game> result = underTestGameRepository.getNotAccepted();
        //then
        List<Game> expected = games.stream().filter(game -> !game.isAccepted()).toList();
        assertThat(result).isEqualTo(expected);
    }
    @Test
    void should_get_not_accepted_games_and_return_empty_list() {

        //given

        //when
        List<Game> result = underTestGameRepository.getNotAccepted();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    private List<Game> gamesToTest(){

        return List.of(
                new Game(null,"Gothic","",null,null,null,true),
                new Game(null,"Risen","",null,null,null,false),
                new Game(null,"Assassin","",null,null,null,true),
                new Game(null,"God of war","",null,null,null,false)
        );
    }
}