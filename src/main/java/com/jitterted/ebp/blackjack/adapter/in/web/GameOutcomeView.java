package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;

import java.util.List;

public class GameOutcomeView {

    public static GameOutcomeView of(Game game) {
        return null;
    }

    public List<PlayerOutcomeView> getPlayerOutcomes() {
        return List.of();
    }
}
