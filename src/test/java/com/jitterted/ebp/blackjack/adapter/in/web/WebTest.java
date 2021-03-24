package com.jitterted.ebp.blackjack.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WebTest {

  @Autowired
  MockMvc mockMvc;

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

}
