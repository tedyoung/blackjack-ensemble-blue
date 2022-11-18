package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@WebMvcTest
public class WebConfigurationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GameService gameService;

    @MockBean
    GameRepository gameRepository;

    @MockBean
    GameMonitor gameMonitor;

    @BeforeEach
    public void initGameService() {
        gameService.createGame(1);
    }

    @Test
    public void getOfHomePageIsStatus200Ok() throws Exception {
        mockMvc.perform(get("/index.html"))
               .andExpect(status().isOk());
    }

    @Test
    public void postToStartGameEndpointIs3xxRedirect() throws Exception {
        mockMvc.perform(post("/start-game")
                                .param("numberOfPlayers", "1"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    public void getOfGameEndpointIs200Ok() throws Exception {
        mockMvc.perform(get("/game"))
               .andExpect(status().isOk());
    }

    @Test
    public void postToHitEndpointIs3xxRedirect() throws Exception {
        mockMvc.perform(post("/hit"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    public void getOfDoneEndpointIs200Ok() throws Exception {
        gameService.currentGame().playerStands(); // need to be in the "game over" state before going to /done
        mockMvc.perform(get("/done"))
               .andExpect(status().isOk());
    }

    @Test
    public void postToStandEndpointIs3xxRedirect() throws Exception {
        mockMvc.perform(post("/stand"))
               .andExpect(status().is3xxRedirection());
    }

}
