package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
        gameService.createGame(List.of(PlayerId.of(1)));
    }

    @Test
    public void getOfHomePageIsStatus200Ok() throws Exception {
        mockMvc.perform(get("/index.html"))
               .andExpect(status().isOk());
    }

    @Test
    public void postToStartGameEndpointIs3xxRedirect() throws Exception {
        mockMvc.perform(post("/create-game")
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
        createGameAndPlaceBets();

        mockMvc.perform(post("/hit"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @Disabled("This is flaky, because the game could already be over after placing bets by getting Blackjack")
    public void getOfDoneEndpointIs200Ok() throws Exception {
        createGameAndPlaceBets();

        gameService.currentGame().playerStands(); // need to be in the "game over" state before going to /done
        mockMvc.perform(get("/done"))
               .andExpect(status().isOk());
    }

    @Test
    public void postToStandEndpointIs3xxRedirect() throws Exception {
        createGameAndPlaceBets();

        mockMvc.perform(post("/stand"))
               .andExpect(status().is3xxRedirection());
    }

    private void createGameAndPlaceBets() throws Exception {
        mockMvc.perform(post("/create-game")
                                .param("numberOfPlayers", "1"));
        mockMvc.perform(post("/place-bets")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("bets[0]", "10"));
    }

}
