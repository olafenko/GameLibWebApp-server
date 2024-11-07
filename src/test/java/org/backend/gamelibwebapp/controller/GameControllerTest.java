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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
class GameControllerTest {

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
    void setUp() {
        gameRepository.deleteAll();
    }


    @Test
    void should_add_new_game() throws Exception {

        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/games/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test game\",\"producer\":\"Test producer\",\"gameCategories\":[\"RPG\"],\"imageUrl\":\"test\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test game"));
        //then

    }

    @Test
    void should_not_add_new_game_cause_it_already_exists() throws Exception {

        //given
        Game testGame = new Game();
        testGame.setTitle("Test game");
        testGame.setAccepted(true);
        gameRepository.save(testGame);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/games/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test game\",\"producer\":\"Test producer\",\"gameCategories\":[\"RPG\"],\"imageUrl\":\"test\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("Game \"Test game\" already exists."));
        //then

    }

    @Test
    void should_update_game() throws Exception {
        //given
        Game testGame = new Game();
        testGame.setTitle("Test game");
        testGame.setAccepted(true);

        gameRepository.save(testGame);
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/games/update/" + testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test game 2\",\"producer\":\"Test producer 2\",\"gameCategories\":[\"MOBA\"],\"imageUrl\":\"test 2\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(Matchers.is("Test game 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.producer").value(Matchers.is("Test producer 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories").value(Matchers.contains("MOBA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(Matchers.is("test 2")));
        //then

    }

    @Test
    void should_not_update_game_because_it_not_exist() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/games/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test game 2\",\"producer\":\"Test producer 2\",\"gameCategories\":[\"MOBA\"],\"imageUrl\":\"test 2\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Game with id 999 not found"));
        //then

    }


    @Test
    void should_delete_game() throws Exception {
        //given
        Game testGame = new Game();
        testGame.setTitle("Test game");
        testGame.setAccepted(true);
        gameRepository.save(testGame);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/games/delete/" + testGame.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(testGame.getTitle() + " deleted successfully."));
        //then
    }

    @Test
    void should_not_delete_game_because_it_not_exist() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/games/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Game with id 1 not found"));
        //then
    }


    @Test
    void should_get_all_games() throws Exception {
        //given
        Game testGame1 = new Game();
        testGame1.setAccepted(true);
        Game testGame2 = new Game();
        testGame2.setAccepted(true);

        gameRepository.save(testGame1);
        gameRepository.save(testGame2);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

        //then
    }

    @Test
    void should_get_top_three_games() throws Exception {
        //given
        Game testGame1 = new Game();
        testGame1.setAccepted(true);
        Game testGame2 = new Game();
        testGame2.setAccepted(true);
        Game testGame3 = new Game();
        testGame3.setAccepted(true);
        Game testGame4 = new Game();
        testGame4.setAccepted(true);

        gameRepository.save(testGame1);
        gameRepository.save(testGame2);
        gameRepository.save(testGame3);
        gameRepository.save(testGame4);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/top-three"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));

        //then
    }


    @Test
    void should_get_single_game_by_id() throws Exception {

        //given
        Game testGame = new Game();
        testGame.setTitle("Test");
        testGame.setAccepted(true);
        gameRepository.save(testGame);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/" + testGame.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("Test")));
        //then

    }

    @Test
    void should_not_get_single_game_because_is_not_found() throws Exception {

        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Game with id 999 not found"));
        //then

    }

    @Test
    void should_not_get_single_game_because_is_not_accepted() throws Exception {
        //given
        Game testGame = new Game();
        testGame.setTitle("Test");
        testGame.setAccepted(false);
        gameRepository.save(testGame);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/" + testGame.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("Cannot get not accepted game"));
        //then
    }

}