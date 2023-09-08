package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private final int playerCount;
    private Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
    private Shoe shoe = new Shoe(List.of(deck));
    private List<PlayerId> playerIds = new ArrayList<>();
    private boolean placeBets = false;
    private boolean initialDeal = false;

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
        return playerCountOf(1)
                .shoe(shoe)
                .addPlayer(playerId)
                .placeBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Shoe shoe) {
        return playerCountOf(1)
                .shoe(shoe)
                .addPlayer(new PlayerId(42))
                .placeBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(new PlayerId(42))
                .placeBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBetsInitialDeal(Deck deck) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(new PlayerId(42))
                .placeBets()
                .initialDeal()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck, PlayerId playerId) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(playerId)
                .placeBets()
                .build();
    }

    public static Game createTwoPlayerGame(PlayerId playerIdOne, PlayerId playerIdTwo) {
        return playerCountOf(2)
                .deck(StubDeckBuilder.buildTwoPlayerFixedDeck())
                .addPlayer(playerIdOne)
                .addPlayer(playerIdTwo)
                .build();
    }

    //
    public static Game createTwoPlayerGamePlaceBets(Deck deck, PlayerId playerOne, PlayerId playerTwo) {
        Game game = playerCountOf(2)
                .deck(deck)
                .addPlayer(playerOne)
                .addPlayer(playerTwo)
                .placeBets()
                .build();

//        List<PlayerId> playerIds = List.of(playerOne, playerTwo);
//        Game game = new Game(new Shoe(List.of(deck)), playerIds);
//        List<PlayerBet> bets = List.of(
//                new PlayerBet(playerOne, new Bet(11)),
//                new PlayerBet(playerTwo, new Bet(22)));
//        game.placePlayerBets(bets);
        return game;
    }

    private GameBuilder initialDeal() {
        initialDeal = true;
        return this;
    }


    private GameBuilder deck(Deck deck) {
        shoe(new Shoe(List.of(deck)));
        return this;
    }

    private GameBuilder shoe(Shoe shoe) {
        this.shoe = shoe;
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
            List<PlayerBet> playerBets = createBets();
            game.placePlayerBets(playerBets);
        }
        if (initialDeal) {
            game.initialDeal();
        }
        return game;
    }

    private List<PlayerBet> createBets() {
        List<PlayerBet> playerBets = new ArrayList<>();
        for (PlayerId playerId : this.playerIds) {
            playerBets.add(new PlayerBet(playerId, Bet.of(42)));
        }
        return playerBets;
    }

    private void requireCorrectPlayerCount() {
        if (playerCount != playerIds.size()) {
            throw new PlayerCountMismatch();
        }
    }

}
