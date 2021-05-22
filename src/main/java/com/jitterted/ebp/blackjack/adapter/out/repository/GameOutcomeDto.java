package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;

public class GameOutcomeDto {
    private Game game;

    public GameOutcomeDto(Game game) {
        this.game = game;
    }

  public GameOutcomeDto() {

  }

  public String asString() {
        return new HandOutcomeDto(game.playerHand()).asString() + ",4♥/5♥/J♥";
    }
}
