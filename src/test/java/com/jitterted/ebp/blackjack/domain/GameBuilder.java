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
    private List<PlayerBet> playerBets = new ArrayList<>();

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

    public static Game createTwoPlayerGamePlaceBets(Deck deck, PlayerId playerOne, PlayerId playerTwo) {
        return playerCountOf(2)
                .deck(deck)
                .addPlayer(playerOne)
                .addPlayer(playerTwo)
                .placeBets()
                .build();
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck, PlayerId playerOne, PlayerId playerTwo) {
        return playerCountOf(2)
                .deck(deck)
                .addPlayer(playerOne)
                .addPlayer(playerTwo)
                .placeBets()
                .initialDeal()
                .build();
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck) {
        return playerCountOf(2)
                .withDefaultPlayers()
                .deck(deck)
                .placeBets()
                .initialDeal()
                .build();
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck, PlayerBet firstPlayerBet,
                                                               PlayerBet secondPlayerBet) {
        Game game = playerCountOf(2)
                .withDefaultPlayers()
                .deck(deck)
                .placeBet(firstPlayerBet)
                .placeBet(secondPlayerBet)
                .initialDeal()
                .build();
//        List<PlayerBet> bets = List.of(firstPlayerBet, secondPlayerBet);
//        Game game = new Game(new Shoe(List.of(deck)),
//                             List.of(firstPlayerBet.playerId(), secondPlayerBet.playerId()));
//        game.placePlayerBets(bets);
//        game.initialDeal();
        return game;
    }


    private GameBuilder withDefaultPlayers() {
        for (int i = 0; i < playerCount; i++) {
            playerIds.add(new PlayerId((i + 1) * 11));
        }
        return this;
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
        playerBets = createBets();
        return this;
    }

    private GameBuilder placeBet(PlayerBet playerBet) {
        placeBets = true;
        playerBets.add(playerBet);
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
