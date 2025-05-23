package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
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
    private final PlayerAccountRepository playerAccountRepository;

    public GameService(GameMonitor gameMonitor,
                       GameRepository gameRepository,
                       Shuffler shuffler,
                       PlayerAccountRepository playerAccountRepository) {
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        this.shuffler = shuffler;
        this.playerAccountRepository = playerAccountRepository;
    }

    public static GameService createForTest(Shuffler shuffler) {
        return new GameService(game -> {
        }, game -> {
        }, shuffler, null);
    }

    public static GameService createForTest(Shuffler shuffler,
                                            PlayerAccountRepository playerAccountRepository) {
        return new GameService(game -> {
        }, game -> {
        }, shuffler, playerAccountRepository);
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
            // loop through all playerIds in the Game:
                // retrieved PlayerAccount from Repository
                    // behind the scenes: read in events and apply to PlayerAccount
                // playerAccount.win(payout, playerOutcome)
                // repository.save(playerAccount)
                    // behind the scenes: write out (new) events to database
        }
    }

    public void placePlayerBets(List<PlayerBet> bets) {
        // for each bet: load PlayerAccount from repository
        // make sure player has enough to bet
        // playerAccount.bet(amount)
        // save PlayerAccount in repository
        currentGame.placePlayerBets(bets);
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
}
