package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.DeckFactory;
import com.jitterted.ebp.blackjack.domain.Game;

public class GameService {

    private final DeckFactory deckFactory;
    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;

    public GameService(DeckFactory deckFactory) {
        this(game -> {
        }, game -> {
        }, deckFactory);
    }

    public GameService(GameMonitor gameMonitor,
                       GameRepository gameRepository,
                       DeckFactory deckFactory) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.deckFactory = deckFactory;
    }

    public void createGame(int numberOfPlayers) {
        currentGame = new Game(numberOfPlayers, deckFactory);
    }

    public void createGame(int numberOfPlayers, Deck deck) {
        currentGame = new Game(numberOfPlayers, new DeckFactory(deck));
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
