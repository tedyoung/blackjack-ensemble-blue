package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.ShuffledDeck;

import java.util.List;

public class GameService {

    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;

    public GameService(GameMonitor gameMonitor,
                       GameRepository gameRepository) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
    }

    public static GameService createForTest() {
        return new GameService(game -> {
        }, game -> {
        });
    }

    public void createGame(int numberOfPlayers) {
        // GOAL: generate new Shoe here, using random number generation Port
        // create N number of Decks using the random numbers to "shuffle" those decks
        Shoe shoe = new Shoe(List.of(new ShuffledDeck()));
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
