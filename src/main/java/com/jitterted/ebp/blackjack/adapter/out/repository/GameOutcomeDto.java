package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Hand;

public class GameOutcomeDto {
  private Game game;

  public GameOutcomeDto(Game game) {
    this.game = game;
  }

  public String asString() {
    return new HandOutcomeDto(game.playerHand()).asString() +
        new HandOutcomeDto(new)
        ",4♥/5♥/J♥";
  }
}
