package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;

public class GameOutcomeDto {
    private Game game;

    public GameOutcomeDto(Game game) {
        this.game = game;
    }

    public String asString() {
        return null;
    }
}
