package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class GameBuilder {
    private GameBuilder() {
    }

    public static GameBuilder playerCountOf(int playerCount) {
        return new GameBuilder();
    }

    public Game build() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        List<PlayerId> playerIds = List.of(new PlayerId(54));
        return new Game(shoe, playerIds);
    }
}
