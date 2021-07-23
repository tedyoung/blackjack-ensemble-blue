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
    private boolean playerDone;

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
        updateGameDoneState(player.hasBlackjack());
    }

    public PlayerOutcome determineOutcome() {
        return outcome(player, dealerHand);
    }

    private PlayerOutcome outcome(Player player, Hand dealerHand) {
        requireGameIsOver();
        if (player.hasBlackjack()) {
            return PlayerOutcome.BLACKJACK;
        }
        if (dealerHand.isBusted()) {
            return PlayerOutcome.DEALER_BUSTED;
        }
        if (player.isBusted()) {
            return PlayerOutcome.PLAYER_BUSTED;
        }
        if (player.pushesWith(dealerHand)) {
            return PlayerOutcome.PLAYER_PUSHES_DEALER;
        }
        if (player.beats(dealerHand)) {
            return PlayerOutcome.PLAYER_BEATS_DEALER;
        }
        return PlayerOutcome.PLAYER_LOSES;
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
        requirePlayerNotDone();
        player.drawFrom(deck);
        updateGameDoneState(player.isBusted());
    }

    public void playerStands() {
        requirePlayerNotDone();
        dealerTurn();
        updateGameDoneState(true);
    }

    public boolean isPlayerDone() {
        return playerDone;
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        player.drawFrom(deck);
        dealerHand.drawFrom(deck);
    }

    private void requireGameIsOver() {
        if (!isPlayerDone()) {
            throw new IllegalStateException();
        }
    }

    private void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        if (!player.isBusted()) {
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(deck);
            }
        }
    }

    private void requirePlayerNotDone() {
        if (isPlayerDone()) {
            throw new IllegalStateException();
        }
    }

    private void updateGameDoneState(boolean gameOver) {
        this.playerDone = gameOver;
        if (gameOver) {
            roundCompleted();
        }
    }

    private void roundCompleted() {
        gameMonitor.roundCompleted(this);
        gameRepository.saveOutcome(this);
    }
}
