package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.Shuffler;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.OrderedDeck;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Shoe;

import java.util.ArrayList;
import java.util.List;

public class GameService {

    private static final int NUMBER_OF_DECKS_IN_SHOE = 4;
    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;
    private Game currentGame;
    private final Shuffler shuffler;

    public GameService(GameMonitor gameMonitor,
                       GameRepository gameRepository,
                       Shuffler shuffler) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.shuffler = shuffler;
    }

    public static GameService createForTest(Shuffler shuffler) {
        return new GameService(game -> {
        }, game -> {
        }, shuffler);
    }

    public void createGame(List<PlayerId> playerIds) {
        currentGame = new Game(playerIds, createShoe());
    }

    public void createGame(List<PlayerId> playerIds, Shoe shoe) {
        currentGame = new Game(playerIds, shoe);
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
            // retrieved PlayerAccount from persistence
            // playerAccount.won(...)
            // playerAccount.newEvents()
            // publish(domainEvents) -- integration events
        }
    }

    private Shoe createShoe() {
        List<Deck> randomDecks = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DECKS_IN_SHOE; i++) {
            randomDecks.add(createRandomDeck());
        }
        return new Shoe(randomDecks);
    }

    private Deck createRandomDeck() {
        return new OrderedDeck(shuffler.create52ShuffledNumbers());
    }

    public List<PlayerBet> currentBets() {
        return currentGame.currentBets();
    }

    public void placePlayerBets(List<PlayerBet> bets) {
        // for each bet: load PlayerAccount from repository
        // playerAccount.bet()
        // save PlayerAccount in repository
        currentGame.placePlayerBets(bets);
    }
}
