package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;

public class GameService {

    private final Deck deck;
    // private final DeckFactory deckFactory;
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
        currentGame = new Game(deck, numberOfPlayers);
    }

    public void createGame(int numberOfPlayers, Deck deck) {
        currentGame = new Game(deck, numberOfPlayers);
    }

    public Game currentGame() {
        if (currentGame == null) {
            throw new IllegalStateException("Game not created");
        }
        return currentGame;
    }

    public void initialDeal() {
        execute(Game::initialDeal);
    }

    public void playerHits() {
        execute(Game::playerHits);
    }

    public void playerStands() {
        execute(Game::playerStands);
    }

    private void execute(GameCommand command) {
        Game game = currentGame();
        command.execute(game);
        if (game.isGameOver()) {
            gameMonitor.gameCompleted(game);
            gameRepository.saveOutcome(game);
        }
    }

}
