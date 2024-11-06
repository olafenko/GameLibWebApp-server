package org.backend.gamelibwebapp.controller;

import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@WithMockUser(username = "admin",roles = {"ADMIN"})
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;

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
    void setUp(){
        gameRepository.deleteAll();
    }

    @Test
    void should_get_games_to_accept() throws Exception {

        //given
        Game accepted = new Game();
        accepted.setAccepted(true);
        Game notAccepted1 = new Game();
        notAccepted1.setAccepted(false);
        Game notAccepted2 = new Game();
        notAccepted2.setAccepted(false);

        gameRepository.save(accepted);
        gameRepository.save(notAccepted1);
        gameRepository.save(notAccepted2);


        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/to-accept"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
        //then

    }

    @Test
    void should_accept_game() throws Exception {

        //given

        Game game = new Game();
        game.setAccepted(false);

        gameRepository.save(game);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/accept/" + game.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accepted", Matchers.equalTo(true)));
        //then
    }
    @Test
    void should_not_accept_game_because_it_not_exist() throws Exception {

        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/accept/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Game with id 999 not found"));
        //then
    }

    @Test
    void should_reject_game() throws Exception {
        //given
        Game testGame = new Game();
        testGame.setTitle("Test game");
        testGame.setAccepted(false);
        gameRepository.save(testGame);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/reject/" + testGame.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(testGame.getTitle() + " deleted successfully."));
        //then
    }
    @Test
    void should_not_reject_game_because_it_not_exist() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/reject/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Game with id 1 not found"));
        //then
    }
}