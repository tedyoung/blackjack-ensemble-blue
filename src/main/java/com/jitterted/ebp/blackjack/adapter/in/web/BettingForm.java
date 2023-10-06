package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerBet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BettingForm {
    private List<Integer> bets = new ArrayList<>();

    public BettingForm() {
    }

    public BettingForm(List<Integer> bets) {
        this.bets = bets;
    }

    public BettingForm(Map<String, String> map) {

    }

    public List<Integer> getBets() {
        return bets;
    }

    public List<PlayerBet> getPlayerBets() {
        return null;
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
