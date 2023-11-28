package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;

public class GameResultDto {

    private final Game game;

    public GameResultDto(Game game) {
        this.game = game;
    }

    public String asString() {
        return "%s,%s,%s".formatted(
                new HandDto(game.currentPlayerCards()).asString(),
                new HandDto(game.dealerHand()).asString(),
                new OutcomeDto(game.currentPlayerOutcome()).asString());
    }
}
