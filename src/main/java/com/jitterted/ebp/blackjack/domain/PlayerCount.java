package com.jitterted.ebp.blackjack.domain;

public record PlayerCount(int playerCount) {

    public PlayerCount {
        if (playerCount < 1) {
            throw new NotEnoughPlayers();
        }

        if (playerCount > 5) {
            throw new TooManyPlayers();
        }
    }
}