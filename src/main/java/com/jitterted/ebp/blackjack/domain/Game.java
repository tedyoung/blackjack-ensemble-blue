package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;
    private final GameRepository gameRepository;

    private final DealerHand dealerHand = new DealerHand();

    public final List<Player> players;
    private final Iterator<Player> playerIterator;
    private Player currentPlayer;
    private final List<PlayerDoneEvent> events = new ArrayList<>();

    public Game(int numberOfPlayers) {
        this(new Deck(), numberOfPlayers);
    }

    public Game(Deck deck, int numberOfPlayers) {
        this(deck, game -> {
        }, numberOfPlayers);
    }

    public Game(Deck deck, GameMonitor gameMonitor, int numberOfPlayers) {
        this(deck, gameMonitor, game -> {
        }, numberOfPlayers);
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
        if (dealerHand.hasBlackjack()) {
            tellAllPlayersAreDoneDealerBlackjack();
        }
        playerStateChanged();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        players.forEach(player -> player.initialDrawFrom(deck));
        dealerHand.drawFrom(deck);
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public PlayerOutcome currentPlayerOutcome() {
        return currentPlayer.outcome(dealerHand);
    }

    public int currentPlayerHandValue() {
        return currentPlayer.handValue();
    }

    public List<Card> currentPlayerCards() {
        // not allowed when game is done (Protocol Violation)
        return currentPlayer.cards();
    }

    public List<PlayerResult> playerResults() {
        return players.stream()
                      .map(player -> new PlayerResult(player, player.outcome(dealerHand)))
                      .collect(Collectors.toList());
    }

    private void playerStateChanged() {
        if (!currentPlayer.isDone()) {
            return;
        }

        addCurrentPlayerToEvents();

        if (haveMorePlayers()) {
            currentPlayer = playerIterator.next();
            playerStateChanged();
        } else {
            dealerTurn();
            gameCompleted();
        }
    }

    private void dealerTurn() {
        if (!haveStandingPlayers()) {
            return;
        }

        dealerHand.flipTheHoleCardUp();

        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        while (dealerHand.dealerMustDrawCard()) {
            dealerHand.drawFrom(deck);
        }
    }

    private boolean haveStandingPlayers() {
        return players.stream()
                      .anyMatch(player -> player.reasonDone().equals(PlayerReasonDone.PLAYER_STANDS));
    }

    private void addCurrentPlayerToEvents() {
        PlayerDoneEvent playerEvent = new PlayerDoneEvent(currentPlayer.id(),
                                                          currentPlayer.reasonDone());
        events.add(playerEvent);
    }

    public boolean isGameOver() {
        return !haveMorePlayers() && currentPlayer.isDone();
    }

    private void tellAllPlayersAreDoneDealerBlackjack() {
        players.forEach(Player::doneDealerDealtBlackjack);
    }

    private boolean haveMorePlayers() {
        return playerIterator.hasNext();
    }

    public void playerHits() {
        currentPlayer.hit(deck);
        playerStateChanged();
    }

    public void playerStands() {
        currentPlayer.stand();
        playerStateChanged();
    }

    private void gameCompleted() {
        gameMonitor.gameCompleted(this);
        gameRepository.saveOutcome(this);
    }

    public int playerCount() {
        return players.size();
    }

    public List<PlayerDoneEvent> events() {
        return events;
    }

    public int currentPlayerId() {
        return currentPlayer.id();
    }

}
