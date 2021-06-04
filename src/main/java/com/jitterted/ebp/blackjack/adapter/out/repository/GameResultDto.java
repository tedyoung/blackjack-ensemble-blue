package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;

public class GameResultDto {

    private Game game;

    public GameResultDto(Game game) {
        this.game = game;
    }

    public String asString() {
        return new HandDto(game.playerHand()).asString() +
                "," +
                new HandDto(game.dealerHand()).asString() + "," + game.determineOutcome();
    }
}
