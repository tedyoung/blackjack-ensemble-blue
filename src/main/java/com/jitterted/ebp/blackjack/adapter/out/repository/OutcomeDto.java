package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.GameOutcome;

public class OutcomeDto {

    private final GameOutcome gameOutcome;

    public OutcomeDto(GameOutcome gameOutcome) {

        this.gameOutcome = gameOutcome;
    }

    public String asString() {
//        PLAYER_BUSTED,
//        DEALER_BUSTED,
//        PLAYER_BEATS_DEALER,
//        PLAYER_PUSHES_DEALER,
//        PLAYER_LOSES,
//        BLACKJACK;
        switch (gameOutcome) {
            case PLAYER_BUSTED:
                return "Player Busted";
            case DEALER_BUSTED:
                return "Dealer Busted";
            case PLAYER_BEATS_DEALER:
                return "Player Beats Dealer";

            case PLAYER_LOSES:
                return "Player Loses";
            case BLACKJACK:
                return "Player Wins Blackjack";
        }
        return "No gameoutcome determined";
    }
}
