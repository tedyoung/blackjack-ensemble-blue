package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.util.List;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;
    private final Player player = new Player();
    private final Hand dealerHand = new Hand();
    private GameRepository gameRepository = game -> {
    };

    public Game() {
        this(new Deck());
    }

    public Game(Deck deck) {
        this(deck, game -> {
        });
    }

    public Game(Deck deck, GameMonitor gameMonitor) {
        this.deck = deck;
        this.gameMonitor = gameMonitor;
    }

    public Game(Deck deck, GameMonitor gameMonitor, GameRepository gameRepository) {
        this.deck = deck;
        this.gameMonitor = gameMonitor;
        this.gameRepository = gameRepository;
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
        playerStateChanged();
    }

    public PlayerOutcome determineOutcome() {
        return player.outcome(dealerHand);
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public int playerHandValue() {
        return player.handValue();
    }

    public List<Card> playerCards() {
        return player.cards();
    }

    public void playerHits() {
        player.hit(deck);
        playerStateChanged();
    }

    public void playerStands() {
        player.stand();
        dealerTurn();
        playerStateChanged();
    }

    public boolean isPlayerDone() {
        return player.isDone();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        player.drawFrom(deck);
        dealerHand.drawFrom(deck);
    }

    private void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        if (!player.isBusted()) {
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(deck);
            }
        }
    }

    private void playerStateChanged() {
        if (player.isDone()) {
            roundCompleted();
        }
    }

    private void roundCompleted() {
        gameMonitor.roundCompleted(this);
        gameRepository.saveOutcome(this);
    }
}
