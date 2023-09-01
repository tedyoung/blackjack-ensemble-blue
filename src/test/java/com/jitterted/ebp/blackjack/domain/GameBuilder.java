package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private final int playerCount;
    private Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
    private Shoe shoe = new Shoe(List.of(deck));
    private List<PlayerId> playerIds = new ArrayList<>();
    private boolean placeBets = false;

    public static Game createOnePlayerGame() {
        return playerCountOf(1)
                .addPlayer(new PlayerId(54))
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(PlayerId playerId) {
        return GameBuilder.playerCountOf(1)
                          .addPlayer(playerId)
                          .placeBets()
                          .build();
    }

    private GameBuilder(int playerCount) {
        this.playerCount = playerCount;
    }

    public static GameBuilder playerCountOf(int playerCount) {
        return new GameBuilder(playerCount);
    }

    public GameBuilder placeBets() {
        placeBets = true;
        return this;
    }

    public GameBuilder addPlayer(PlayerId playerId) {
        playerIds.add(playerId);
        return this;
    }

    public Game build() {
        if (playerCount != playerIds.size()) {
            throw new PlayerCountMismatch();
        }

        Game game = new Game(shoe, playerIds);
        if (placeBets) {
            List<PlayerBet> playerBets = List.of(new PlayerBet(playerIds.get(0), Bet.of(42)));
            game.placePlayerBets(playerBets);
        }
        return game;
    }

}
