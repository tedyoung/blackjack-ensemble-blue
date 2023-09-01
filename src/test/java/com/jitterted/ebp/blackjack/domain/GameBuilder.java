package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private static final Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
    private static Shoe shoe = new Shoe(List.of(deck));
    private static List<PlayerBet> playerBets;
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
        Game game = new Game(shoe, List.of(playerId));
        playerBets = List.of(new PlayerBet(playerId, Bet.of(42)));
        game.placePlayerBets(playerBets);
        return game;
    }

    public Game build() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        playerIds.add(new PlayerId(54));
        return new Game(shoe, playerIds);
    }

}
