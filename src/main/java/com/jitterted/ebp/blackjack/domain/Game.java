package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;
    private GameRepository gameRepository;

    private final DealerHand dealerHand = new DealerHand();

    public final List<Player> players;
    private final Iterator<Player> playerIterator;
    private Player currentPlayer;
    private List<PlayerDoneEvent> events = new ArrayList<>();

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
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
        while (dealerHand.dealerMustDrawCard()) {
            dealerHand.drawFrom(deck);
        }
    }

    private boolean haveStandingPlayers() {
        return players.stream()
                      .anyMatch(player -> player.reasonDone().equals("Player stands"));
    }

    private void addCurrentPlayerToEvents() {
        PlayerDoneEvent playerEvent = new PlayerDoneEvent(currentPlayer.id(),
                                                          currentPlayer.reasonDone());
        events.add(playerEvent);
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

    public boolean isGameOver() {
        return !haveMorePlayers() && currentPlayer.isDone();
    }

    public int currentPlayerId() {
        return currentPlayer.id();
    }
}
