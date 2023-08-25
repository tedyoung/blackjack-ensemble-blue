package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private List<PlayerId> playerIds = new ArrayList<>();

    private GameBuilder() {
    }

    public static GameBuilder playerCountOf(int playerCount) {
        return new GameBuilder();
    }

    public Game build() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        addPlayer();
        return new Game(shoe, playerIds);
    }

    private void addPlayer() {
        playerIds.add(new PlayerId(54));
    }
}
