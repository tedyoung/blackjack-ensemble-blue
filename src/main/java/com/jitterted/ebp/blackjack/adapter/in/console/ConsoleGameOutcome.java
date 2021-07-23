package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.domain.PlayerOutcome;

class ConsoleGameOutcome {

    // transform Enum to String
    static String of(PlayerOutcome gameOutcome) {
        if (gameOutcome == PlayerOutcome.PLAYER_BUSTED) {
            return "You Busted, so you lose.  💸";
        } else if (gameOutcome == PlayerOutcome.DEALER_BUSTED) {
            return "Dealer went BUST, Player wins! Yay for you!! 💵";
        } else if (gameOutcome == PlayerOutcome.PLAYER_BEATS_DEALER) {
            return "You beat the Dealer! 💵";
        } else if (gameOutcome == PlayerOutcome.PLAYER_PUSHES_DEALER) {
            return "Push: The house wins, you Lose. 💸";
        } else if (gameOutcome == PlayerOutcome.PLAYER_LOSES) {
            return "You lost to the Dealer. 💸";
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
