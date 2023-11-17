package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BettingForm {
    @Deprecated // the new HTML form uses the Map instead of separate lists
    private List<Integer> playerIds = new ArrayList<>();

    @Deprecated // the new HTML form uses the Map instead of separate lists
    private List<Integer> bets = new ArrayList<>();

    private Map<String, String> playerIdToBets;

    public BettingForm() {
    }

    @Deprecated
    public BettingForm(List<Integer> bets) {
        this.bets = bets;
    }

    public BettingForm(Map<String, String> playerIdToBets) {
        this.playerIdToBets = playerIdToBets;
    }

    @Deprecated
    public List<Integer> getBets() {
        return bets;
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

    public void setBets(List<Integer> bets) {
        this.bets = bets;
    }

    @Override
    public String toString() {
        return "BettingForm[" +
                "bets=" + bets + ']';
    }

    @Deprecated // the new HTML form uses the Map instead of this list
    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    @Deprecated // the new HTML form uses the Map instead of this list
    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
    }

    public Map<String, String> getPlayerIdToBets() {
        return playerIdToBets;
    }

    public void setPlayerIdToBets(Map<String, String> playerIdToBets) {
        this.playerIdToBets = playerIdToBets;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BettingForm) obj;
        return Objects.equals(this.bets, that.bets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bets);
    }

}
