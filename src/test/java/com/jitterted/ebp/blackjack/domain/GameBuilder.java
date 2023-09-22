package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private final int playerCount;
    private Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
    private Shoe shoe = new Shoe(List.of(deck));
    private List<PlayerId> playerIds = new ArrayList<>();
    private boolean initialDeal = false;
    private List<PlayerBet> playerBets = new ArrayList<>();

    public static Game createOnePlayerGame() {
        return playerCountOf(1)
                .addPlayer(new PlayerId(54))
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(PlayerId playerId) {
        return GameBuilder.playerCountOf(1)
                          .addPlayer(playerId)
                          .placeDefaultBets()
                          .build();
    }

    static Game createOnePlayerGamePlaceBets(Shoe shoe, PlayerId playerId) {
        return playerCountOf(1)
                .shoe(shoe)
                .addPlayer(playerId)
                .placeDefaultBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Shoe shoe) {
        return playerCountOf(1)
                .shoe(shoe)
                .addPlayer(new PlayerId(42))
                .placeDefaultBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(new PlayerId(42))
                .placeDefaultBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBetsInitialDeal(Deck deck) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(new PlayerId(42))
                .placeDefaultBets()
                .initialDeal()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck, PlayerId playerId) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(playerId)
                .placeDefaultBets()
                .build();
    }

    public static Game createTwoPlayerGame(PlayerId playerIdOne, PlayerId playerIdTwo) {
        return playerCountOf(2)
                .deck(StubDeckBuilder.buildTwoPlayerFixedDeck())
                .addPlayer(playerIdOne)
                .addPlayer(playerIdTwo)
                .build();
    }

    public static Game createTwoPlayerGamePlaceBets(Deck deck, PlayerId playerOne, PlayerId playerTwo) {
        return playerCountOf(2)
                .deck(deck)
                .addPlayer(playerOne)
                .addPlayer(playerTwo)
                .placeDefaultBets()
                .build();
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck, PlayerId playerOne, PlayerId playerTwo) {
        return playerCountOf(2)
                .deck(deck)
                .addPlayer(playerOne)
                .addPlayer(playerTwo)
                .placeDefaultBets()
                .initialDeal()
                .build();
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck) {
        return playerCountOf(2)
                .withDefaultPlayers()
                .deck(deck)
                .placeDefaultBets()
                .initialDeal()
                .build();
    }


    public GameBuilder withDefaultPlayers() {
        for (int i = 0; i < playerCount; i++) {
            playerIds.add(new PlayerId((i + 1) * 11));
        }
        return this;
    }

    public GameBuilder initialDeal() {
        initialDeal = true;
        return this;
    }

    public GameBuilder deck(Deck deck) {
        shoe(new Shoe(List.of(deck)));
        return this;
    }

    public GameBuilder shoe(Shoe shoe) {
        this.shoe = shoe;
        return this;
    }

    private GameBuilder(int playerCount) {
        this.playerCount = playerCount;
    }

    public static GameBuilder playerCountOf(int playerCount) {
        return new GameBuilder(playerCount);
    }

    public GameBuilder placeDefaultBets() {
        playerBets = createBets();
        return this;
    }

    public GameBuilder addPlayer(PlayerId playerId) {
        playerIds.add(playerId);
        return this;
    }

    public GameBuilder addPlayer(PlayerId playerId, Bet bet) {
        addPlayer(playerId);
        return placeBet(new PlayerBet(playerId, bet));
    }

    private GameBuilder placeBet(PlayerBet playerBet) {
        playerBets.add(playerBet);
        return this;
    }

    public Game build() {
        requireCorrectPlayerCount();

        Game game = new Game(shoe, playerIds);
        if (!playerBets.isEmpty()) {
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
            throw new PlayerCountMismatch(String.format(
                    "PlayerCount is %d, but %d players were added.", playerCount, playerIds.size()
            ));
        }
    }
}
