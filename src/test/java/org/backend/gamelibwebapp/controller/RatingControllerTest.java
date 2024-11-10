package org.backend.gamelibwebapp.controller;

import org.backend.gamelibwebapp.entities.AppUser;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.entities.GameRating;
import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.repositories.GameRatingRepository;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
class RatingControllerTest {

    @Autowired
    private GameRatingRepository gameRatingRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private MockMvc mockMvc;

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

    @BeforeEach
    void setUp() {
        gameRatingRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
    }


    @Test
    void should_add_new_rate() throws Exception {

        //given
        Game game = new Game();
        AppUser user = new AppUser();
        game.setAccepted(true);
        gameRepository.save(game);
        appUserRepository.save(user);

        //when

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ratings/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"value\":4.0,\"gameId\":%d,\"userId\":%s}", game.getId(), user.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("4.0"));

        //then
    }

    @Test
    void should_not_add_new_rate_because_game_not_exist() throws Exception {

        //given

        AppUser user = new AppUser();
        appUserRepository.save(user);

        //when

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ratings/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"value\":4.0,\"gameId\":%d,\"userId\":%s}", 999, user.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Game with id 999 not found"));

        //then
    }

    @Test
    void should_not_add_new_rate_because_user_not_exist() throws Exception {

        //given

        Game game = new Game();
        game.setAccepted(true);
        gameRepository.save(game);

        //when

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ratings/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"value\":4.0,\"gameId\":%d,\"userId\":%s}", game.getId(), 999)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User with id 999 not found"));

        //then
    }

    @Test
    void should_not_add_new_rate_because_game_is_not_accepted() throws Exception {

        //given

        Game game = new Game();
        AppUser user = new AppUser();
        game.setAccepted(false);
        gameRepository.save(game);
        appUserRepository.save(user);

        //when

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ratings/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"value\":4.0,\"gameId\":%d,\"userId\":%s}", game.getId(), user.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("Cannot rate not accepted game."));

        //then
    }

    @Test
    void should_update_existing_rate() throws Exception {

        //given

        Game game = new Game();
        AppUser user = new AppUser();
        game.setAccepted(true);
        gameRepository.save(game);
        appUserRepository.save(user);
        GameRating gameRating = new GameRating();
        gameRating.setRate(4.0);
        gameRating.setGame(game);
        gameRating.setUser(user);
        gameRatingRepository.save(gameRating);

        //when

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ratings/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"value\":5.0,\"gameId\":%d,\"userId\":%s}", game.getId(), user.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5.0"));

        //then
    }
}