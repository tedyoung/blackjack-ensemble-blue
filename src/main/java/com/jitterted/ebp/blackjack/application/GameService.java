package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Shoe;

public class GameService {

    private final Shoe shoe;
    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;

    public GameService(Shoe shoe) {
        this(game -> {
        }, game -> {
        }, shoe);
    }

    public GameService(GameMonitor gameMonitor,
                       GameRepository gameRepository,
                       Shoe shoe) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.shoe = shoe;
    }

    public void createGame(int numberOfPlayers) {
        currentGame = new Game(numberOfPlayers, shoe);
    }

    public void createGame(int numberOfPlayers, Shoe shoe) {
        currentGame = new Game(numberOfPlayers, shoe);
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
