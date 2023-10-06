package com.jitterted.ebp.blackjack.adapter.in.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class BettingFormTest {
    @Test
    void passInMapReturnsListOfPlayerBets() {
        Map<String, String> playerBetsMap = Map.of("2", "2");
        BettingForm bettingForm = new BettingForm(playerBetsMap);
    }
}