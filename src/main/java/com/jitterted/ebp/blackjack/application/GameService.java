package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.Shuffler;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.OrderedDeck;
import com.jitterted.ebp.blackjack.domain.Shoe;

import java.util.ArrayList;
import java.util.List;

public class GameService {

    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;
    private Shuffler shuffler;

    public GameService(GameMonitor gameMonitor,
                       GameRepository gameRepository) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.shuffler = () -> {
            List<Integer> cardOrderIndexes = new ArrayList<>();
            for (int i = 0; i < 52; i++) {
                cardOrderIndexes.add(i);
            }
            return cardOrderIndexes;
        };
    }

    public static GameService createForTest() {
        return new GameService(game -> {
        }, game -> {
        });
    }

    public void createGame(int numberOfPlayers) {
        // GOAL: talk to shuffler (Port)
        // create N number of Decks using the random numbers to "shuffle" those decks
        Shoe shoe = new Shoe(List.of(new OrderedDeck(shuffler.create52ShuffledNumbers())));
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
