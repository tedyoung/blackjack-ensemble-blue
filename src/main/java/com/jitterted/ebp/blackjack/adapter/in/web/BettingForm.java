package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BettingForm {
    private Map<String, String> playerIdToBets;
    private List<Integer> bets = new ArrayList<>();

    public BettingForm() {
    }

    public BettingForm(List<Integer> bets) {
        this.bets = bets;
    }

    public BettingForm(Map<String, String> playerIdToBets) {
        this.playerIdToBets = playerIdToBets;
    }

    public List<Integer> getBets() {
        return bets;
    }

    public List<PlayerBet> getPlayerBets() {
        int id = Integer.parseInt(playerIdToBets.get(0));
        int amount = Integer.parseInt(playerIdToBets.get(1));
        return List.of(new PlayerBet(PlayerId.of(id), Bet.of(amount)));
    }

    public void setBets(List<Integer> bets) {
        this.bets = bets;
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

    @Override
    public String toString() {
        return "BettingForm[" +
                "bets=" + bets + ']';
    }

}
