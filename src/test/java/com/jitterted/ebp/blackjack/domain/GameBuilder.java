package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private final int playerCount;
    private Shoe shoe;
    private final List<PlayerId> playerIds = new ArrayList<>();
    private boolean initialDeal = false;
    private List<PlayerBet> playerBets = new ArrayList<>();

    public static Game createOnePlayerGame() {
        return playerCountOf(1)
                .addPlayer(PlayerId.of(54))
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(PlayerId playerId) {
        return GameBuilder.playerCountOf(1)
                          .addPlayer(playerId)
                          .placeDefaultBets()
                          .build();
    }

    public static Game createOnePlayerGamePlaceBets(Shoe shoe) {
        return playerCountOf(1)
                .shoe(shoe)
                .addPlayer(PlayerId.of(42))
                .placeDefaultBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(PlayerId.of(42))
                .placeDefaultBets()
                .build();
    }

    public static Game createOnePlayerGamePlaceBetsInitialDeal(Deck deck) {
        return playerCountOf(1)
                .deck(deck)
                .addPlayer(PlayerId.of(42))
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

    public static Game createTwoPlayerGameWithPlayersInRepository(PlayerAccountRepository playerAccountRepository,
                                                                  StubDeck deck) {
        return createTwoPlayerGameWithPlayersInRepository(playerAccountRepository, deck, "First Player", "Second Player");
    }

    public static Game createTwoPlayerGameWithPlayersInRepository(PlayerAccountRepository playerAccountRepository,
                                                                  StubDeck deck,
                                                                  String firstPlayerName,
                                                                  String secondPlayerName) {
        PlayerId firstPlayerId = playerAccountRepository.save(PlayerAccount.register(firstPlayerName)).getPlayerId();
        PlayerId secondPlayerId = playerAccountRepository.save(PlayerAccount.register(secondPlayerName)).getPlayerId();
        return createTwoPlayerGamePlaceBetsInitialDeal(deck, firstPlayerId, secondPlayerId);
    }

    // Builder Methods
    public GameBuilder withDefaultPlayers() {
        for (int i = 0; i < playerCount; i++) {
            playerIds.add(PlayerId.of((i + 1) * 11));
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
        shoe = new Shoe(List.of(StubDeckBuilder.buildOnePlayerFixedDeck()));
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

        Game game = new Game(playerIds, shoe);
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
            throw new PlayerCountMismatch(
                    "PlayerCount is %d, but %d players were added.".formatted(playerCount, playerIds.size()
            ));
        }
    }
}
