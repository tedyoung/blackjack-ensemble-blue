package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;
    private GameRepository gameRepository;

    private final Hand dealerHand = new Hand();

    private final List<Player> players;
    private final Iterator<Player> playerIterator;
    private Player currentPlayer;
    private List<PlayerEvent> events = new ArrayList<>();

    public Game() {
        this(new Deck());
    }

    public Game(Deck deck) {
        this(deck, game -> {
        });
    }

    public Game(Deck deck, int numberOfPlayers) {
        this(deck, game -> {}, game -> {}, numberOfPlayers);
    }

    public Game(Deck deck, GameMonitor gameMonitor) {
        this(deck, gameMonitor, game -> {});
    }

    public Game(Deck deck, GameMonitor gameMonitor, GameRepository gameRepository) {
        this(deck, gameMonitor, gameRepository, 1);
    }

    public Game(Deck deck, GameMonitor gameMonitor, int numberOfPlayers) {
        this(deck, gameMonitor, game -> {}, numberOfPlayers);
    }

    public Game(Deck deck, GameMonitor gameMonitor, GameRepository gameRepository, int numberOfPlayers) {
        this.deck = deck;
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i));
        }
        playerIterator = players.listIterator();
        currentPlayer = playerIterator.next();
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
        playerStateChanged();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        players.forEach(player -> player.initialDrawFrom(deck));
        dealerHand.drawFrom(deck);
    }

    public PlayerOutcome determineOutcome() {
        return getCurrentPlayer().outcome(dealerHand);
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public int playerHandValue() {
        return getCurrentPlayer().handValue();
    }

    public List<Card> playerCards() {
        return getCurrentPlayer().cards();
    }

    @Deprecated
    // Breaks encapsulation!
    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void playerStateChanged() {
        if (!getCurrentPlayer().isDone()) {
            return;
        }

        PlayerEvent playerEvent = new PlayerEvent(getCurrentPlayer().id(),
                                                  getCurrentPlayer().reasonDone());
        events.add(playerEvent);
        if (haveMorePlayers()) {
            currentPlayer = playerIterator.next();
        } else {
            gameCompleted();
        }
    }

    private boolean haveMorePlayers() {
        return playerIterator.hasNext();
    }

    public void playerHits() {
        getCurrentPlayer().hit(deck);
        playerStateChanged();
    }

    public void playerStands() {
        getCurrentPlayer().stand();
        dealerTurn();
        playerStateChanged();
    }

    // Player is done when:
    // - player is dealt blackjack
    // - player stands
    // - player goes bust
    public boolean isPlayerDone() {
        return getCurrentPlayer().isDone();
    }

    private void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        while (dealerHand.dealerMustDrawCard()) {
            dealerHand.drawFrom(deck);
        }
    }

    private void gameCompleted() {
        gameMonitor.gameCompleted(this);
        gameRepository.saveOutcome(this);
    }

    public int playerCount() {
        return players.size();
    }

    public List<PlayerEvent> events() {
        return events;
    }

    public boolean isGameOver() {
        return false;
    }
}
