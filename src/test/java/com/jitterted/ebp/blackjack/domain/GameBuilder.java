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

    static Game createOnePlayerGamePlaceBets(Shoe shoe, PlayerId playerId) {
        // TODO: resume here with specifying the shoe
        Game game = GameBuilder.playerCountOf(1)
                               .shoe(shoe)
                               .addPlayer(playerId)
                               .placeBets()
                               .build();
        game = new Game(shoe, List.of(playerId));
        List<PlayerBet> bets = List.of(new PlayerBet(playerId, Bet.of(3)));
        game.placePlayerBets(bets);
        return game;
    }

    private GameBuilder shoe(Shoe shoe) {
        return this;
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
        requireCorrectPlayerCount();

        Game game = new Game(shoe, playerIds);
        if (placeBets) {
            List<PlayerBet> playerBets = List.of(new PlayerBet(playerIds.get(0), Bet.of(42)));
            game.placePlayerBets(playerBets);
        }
        return game;
    }

    private void requireCorrectPlayerCount() {
        if (playerCount != playerIds.size()) {
            throw new PlayerCountMismatch();
        }
    }

}
