package org.backend.gamelibwebapp.controller;

import jakarta.transaction.Transactional;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;

    @Test
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(2)));

        //then
    }
    @Test
    @Transactional
    void should_top_three_games() throws Exception {
        //given
        Game testGame1 = new Game();
        testGame1.setAccepted(true);
        Game testGame2 = new Game();
        testGame2.setAccepted(true);
        Game testGame3 = new Game();
        testGame3.setAccepted(true);

        gameRepository.save(testGame1);
        gameRepository.save(testGame2);
        gameRepository.save(testGame3);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(3)));

        //then
    }



    @Test
    @Transactional
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
                .andExpect(jsonPath("$.title",Matchers.is("Test")));
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
    @Transactional
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