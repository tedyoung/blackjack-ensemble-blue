package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.domain.GameOutcome;

class ConsoleGameOutcome {

  // transform Enum to String
  static String of(GameOutcome gameOutcome) {
    if (gameOutcome == GameOutcome.PLAYER_BUSTED) {
      return "You Busted, so you lose.  💸";
    } else if (gameOutcome == GameOutcome.DEALER_BUSTED) {
      return "Dealer went BUST, Player wins! Yay for you!! 💵";
    } else if (gameOutcome == GameOutcome.PLAYER_BEATS_DEALER) {
      return "You beat the Dealer! 💵";
    } else if (gameOutcome == GameOutcome.PLAYER_PUSHES_DEALER) {
      return "Push: The house wins, you Lose. 💸";
    } else if (gameOutcome == GameOutcome.PLAYER_LOSES) {
      return "You lost to the Dealer. 💸";
    } else {
      throw new UnsupportedOperationException();
    }
  }

}
