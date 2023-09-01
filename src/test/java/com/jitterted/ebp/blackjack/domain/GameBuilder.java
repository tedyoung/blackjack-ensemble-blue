package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
    private Shoe shoe = new Shoe(List.of(deck));
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
        Game game = GameBuilder.playerCountOf(1)
                                             .addPlayer(playerId)
                                             .placeBets()
                                             .build();
        game = new Game(shoe, List.of(playerId));
        List<PlayerBet> playerBets = List.of(new PlayerBet(playerId, Bet.of(42)));
        game.placePlayerBets(playerBets);
        return game;
    }

    private GameBuilder placeBets() {
        return this;
    }

    private GameBuilder addPlayer(PlayerId playerId) {
        playerIds.add(playerId);
        return this;
    }

    public Game build() {
        playerIds.add(new PlayerId(54));
        return new Game(shoe, playerIds);
    }

}
