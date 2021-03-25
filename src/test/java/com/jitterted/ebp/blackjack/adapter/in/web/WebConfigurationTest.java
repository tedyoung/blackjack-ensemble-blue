package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WebConfigurationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  GameService gameService;

  @BeforeEach
  public void initGameService() {
    gameService.createGame();
  }

  @Test
  public void getOfHomePageIsStatus200Ok() throws Exception {
    mockMvc.perform(get("/index.html"))
           .andExpect(status().isOk());
  }

  @Test
  public void postToStartGameEndpointIs3xxRedirect() throws Exception {
    mockMvc.perform(post("/start-game"))
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
    mockMvc.perform(get("/done"))
           .andExpect(status().isOk());
  }

  @Test
  public void postToStandEndpointIs3xxRedirect() throws Exception {
    mockMvc.perform(post("/stand"))
           .andExpect(status().is3xxRedirection());
  }

}
