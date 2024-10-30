package org.backend.gamelibwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.gamelibwebapp.dto.GameDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;


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
    void should_get_single_game_by_id() throws Exception {

        //given
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/games/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        GameDTO gameDTO = mapper.readValue(mvcResult.getResponse().getContentAsString(), GameDTO.class);
        assertThat(gameDTO).isNotNull();
        assertThat(gameDTO.title()).isEqualTo("Gothic 3");

    }
}