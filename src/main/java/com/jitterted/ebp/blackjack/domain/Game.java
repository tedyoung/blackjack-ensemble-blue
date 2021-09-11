package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;

    private final Hand dealerHand = new Hand();
    private final List<Player> players;
    private GameRepository gameRepository;
    private int currentPlayerIndex;

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
        currentPlayerIndex = 0;
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        players.forEach(player -> player.drawFrom(deck));
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
        return players.get(currentPlayerIndex);
    }

    public void nextPlayer() {
        currentPlayerIndex++;
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

    public boolean isPlayerDone() {
        return getCurrentPlayer().isDone();
    }

    private void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        while (dealerHand.dealerMustDrawCard()) {
            dealerHand.drawFrom(deck);
        }
    }

    private void playerStateChanged() {
        if (getCurrentPlayer().isDone()) {
            roundCompleted();
        }
    }

    private void roundCompleted() {
        gameMonitor.roundCompleted(this);
        gameRepository.saveOutcome(this);
    }

    public int playerCount() {
        return players.size();
    }
}
