package org.backend.gamelibwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.backend.gamelibwebapp.entities.Game;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private GameRepository gameRepository;


    @Test
    void should_add_game(){
        //given


        //when
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/games/add"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(201))
//                .andExpect(MockMvcResultMatchers.)



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