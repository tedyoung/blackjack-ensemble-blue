package com.jitterted.ebp.blackjack.adapter.in.web;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.Test;

class BettingFormTest {
    @Test
    void passInMapReturnsListOfPlayerBets() {
        Map<String, String> playerBetsMap = Map.of("25", "55");
        BettingForm bettingForm = new BettingForm(playerBetsMap);

        List<PlayerBet> playerBets = bettingForm.getPlayerBets();

        assertThat(playerBets).containsExactly(
                new PlayerBet(PlayerId.of(25), Bet.of(55)));
    }
}