package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BettingForm {

    private Map<String, String> playerIdToBets;

    public BettingForm() {
    }

    public BettingForm(Map<String, String> playerIdToBets) {
        this.playerIdToBets = playerIdToBets;
    }

    public static BettingForm zeroBetsFor(Game game) {
        Map<String, String> playerBets = new HashMap<>();
        for (PlayerId playerId : game.playerIds()) {
            playerBets.put(String.valueOf(playerId.id()), "0");
        }
        return new BettingForm(playerBets);
    }

    // Transforms the Adapter/HTML Form content to Domain Objects for us
    public List<PlayerBet> getPlayerBets() {
        List<PlayerBet> playerBets = new ArrayList<>();
        playerIdToBets.forEach((key, value) -> {
            int id = Integer.parseInt(key);
            int amount = Integer.parseInt(value);
            playerBets.add(new PlayerBet(PlayerId.of(id), Bet.of(amount)));
        });
        return playerBets;
    }

    public Map<String, String> getPlayerIdToBets() {
        return playerIdToBets;
    }

    public void setPlayerIdToBets(Map<String, String> playerIdToBets) {
        this.playerIdToBets = playerIdToBets;
    }


}
