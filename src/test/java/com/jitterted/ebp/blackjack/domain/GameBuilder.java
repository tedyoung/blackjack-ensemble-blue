package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private static final Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
    private List<PlayerId> playerIds = new ArrayList<>();

    public static Game createOnePlayerGame() {
        return playerCountOf(1).build();
    }

    private GameBuilder() {
    }

    public static GameBuilder playerCountOf(int playerCount) {
        return new GameBuilder();
    }

    public static Game createOnePlayerGamePlaceBets(PlayerId playerId) {
        // GameBuilder.playerCountOf(1)
        //            .addPlayer(playerId)
        //            .placeBets()
        //            .build();
        Shoe shoe = new Shoe(List.of(deck));
        Game game = new Game(shoe, List.of(playerId));
        game.placePlayerBets(List.of(new PlayerBet(playerId, Bet.of(42))));
        return game;
    }

    public Game build() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        playerIds.add(new PlayerId(54));
        return new Game(shoe, playerIds);
    }

}
