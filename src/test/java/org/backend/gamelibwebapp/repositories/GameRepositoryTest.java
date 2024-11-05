package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GameRepositoryTest {

    @Autowired
    private GameRepository underTestGameRepository;

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }



    @AfterEach
    void tearDown() {
        underTestGameRepository.deleteAll();
    }

    @Test
    void it_should_check_if_game_exists_by_title_and_return_true() {

        //given
        String title = "Gothic";
        Game game = new Game(null,"Gothic",null,null,null,true);
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

}