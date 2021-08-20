package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;

    private final Player currentPlayer;

    private final Hand dealerHand = new Hand();
    private final List<Player> players;
    private GameRepository gameRepository;

    public Game() {
        this(new Deck());
    }

    public Game(Deck deck) {
        this(deck, game -> {
        });
    }

    public Game(Deck deck, int numOfPlayers) {
        this(deck, game -> {}, game -> {}, numOfPlayers);
    }

    public Game(Deck deck, GameMonitor gameMonitor) {
        this(deck, gameMonitor, game -> {});
    }

    public Game(Deck deck, GameMonitor gameMonitor, GameRepository gameRepository) {
        this(deck, gameMonitor, gameRepository, 1);
    }

    public Game(Deck deck, GameMonitor gameMonitor, GameRepository gameRepository, int numberOfPlayers) {
        this.deck = deck;
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
        players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player());
        }
        currentPlayer = players.get(0);
    }

    public void initialDeal() {
        // How to support 2+ players?
        dealRoundOfCards();
        dealRoundOfCards();
        playerStateChanged();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        players.forEach(player -> player.drawFrom(deck));
        dealerHand.drawFrom(deck);
    }

    public PlayerOutcome determineOutcome() {
        return currentPlayer.outcome(dealerHand);
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public int playerHandValue() {
        return currentPlayer.handValue();
    }

    public List<Card> playerCards() {
        return currentPlayer.cards();
    }

    @Deprecated
    // Breaks encapsulation!
    public List<Player> getPlayers() {
        return players;
    }

    public void playerHits() {
        currentPlayer.hit(deck);
        playerStateChanged();
    }

    public void playerStands() {
        currentPlayer.stand();
        dealerTurn();
        playerStateChanged();
    }

    public boolean isPlayerDone() {
        return currentPlayer.isDone();
    }

    private void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        while (dealerHand.dealerMustDrawCard()) {
            dealerHand.drawFrom(deck);
        }
    }

    private void playerStateChanged() {
        if (currentPlayer.isDone()) {
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
