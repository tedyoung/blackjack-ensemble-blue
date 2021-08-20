package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

public class GameService {

    private final Deck deck;
    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;

    public GameService() {
        this(new Deck());
    }

    public GameService(Deck deck) {
        this.deck = deck;
        this.gameMonitor = game -> {
        };
        this.gameRepository = game -> {
        };
    }

    public GameService(GameMonitor gameMonitor) {
        this.gameMonitor = gameMonitor;
        this.deck = new Deck();
        this.gameRepository = game -> {
        };
    }

    public GameService(GameMonitor gameMonitor, GameRepository gameRepository) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.deck = new Deck();
    }

    public void createGame(int numberOfPlayers) {
        currentGame = new Game(deck, gameMonitor, gameRepository, numberOfPlayers);
    }

    public Game currentGame() {
        if (currentGame == null) {
            throw new IllegalStateException("Game not created");
        }
        return currentGame;
    }

}
