package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.port.GameRepository;

public class GameService {

    private final Deck deck;
    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;

    public GameService(Deck deck) {
        this.deck = deck;
        this.gameMonitor = game -> {
        };
        this.gameRepository = game -> {
        };
    }

    public GameService(GameMonitor gameMonitor, GameRepository gameRepository, Deck deck) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.deck = deck;
    }

    public void createGame(int numberOfPlayers) {
        currentGame = new Game(deck, gameMonitor, gameRepository, numberOfPlayers);
    }

    public void createGame(int numberOfPlayers, Deck deck) {
        currentGame = new Game(deck, gameMonitor, gameRepository, numberOfPlayers);
    }

    public Game currentGame() {
        if (currentGame == null) {
            throw new IllegalStateException("Game not created");
        }
        return currentGame;
    }

    public Game initialDeal() {
        Game game = currentGame();
        game.initialDeal();
        return game;
    }

    public Game playerHits() {
        Game game = currentGame();
        game.playerHits();
        return game;
    }
}
