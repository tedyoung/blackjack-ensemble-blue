package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.PlayerOutcome;

public class OutcomeDto {

    private final PlayerOutcome gameOutcome;

    public OutcomeDto(PlayerOutcome gameOutcome) {
        this.gameOutcome = gameOutcome;
    }

    public String asString() {
        switch (gameOutcome) {
            case PLAYER_BUSTED:
                return "Player Busted";
            case DEALER_BUSTED:
                return "Dealer Busted";
            case PLAYER_BEATS_DEALER:
                return "Player Beats Dealer";
            case PLAYER_PUSHES_DEALER:
                return "Player Pushes Dealer";
            case PLAYER_LOSES:
                return "Player Loses";
            case BLACKJACK:
                return "Player Wins Blackjack";
            default:
                throw new IllegalStateException("No game outcome determined");
        }
    }
}
