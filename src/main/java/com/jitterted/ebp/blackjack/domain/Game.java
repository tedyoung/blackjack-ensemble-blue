package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private final DealerHand dealerHand = new DealerHand();
    private final List<Player> players;
    private final Iterator<Player> playerIterator;
    private Player currentPlayer;
    private final List<PlayerDoneEvent> events = new ArrayList<>();
    private final Shoe shoe;
    private List<Integer> bets = Collections.emptyList();

    public Game(PlayerCount numberOfPlayers, Shoe shoe) {
        this.shoe = shoe;
        players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers.playerCount(); i++) {
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
        players.forEach(player -> {
            player.initialDrawFrom(shoe);
        });
        dealerHand.drawFrom(shoe);
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
        // not allowed to ask before initial deal
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
        }
    }

    public boolean isGameOver() {
        return !haveMorePlayers() && currentPlayer.isDone();
    }

    private void dealerTurn() {
        dealerHand.flipTheHoleCardUp();

        if (playersHaveUnknownOutcome()) {// Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(shoe);
            }
        }
    }

    private boolean playersHaveUnknownOutcome() {
        return players.stream()
                      .anyMatch(player -> player.reasonDone().equals(PlayerReasonDone.PLAYER_STANDS));
    }

    private void addCurrentPlayerToEvents() {
        PlayerDoneEvent playerEvent = new PlayerDoneEvent(currentPlayer.id(),
                                                          currentPlayer.reasonDone());
        events.add(playerEvent);
    }

    private void tellAllPlayersAreDoneDealerBlackjack() {
        players.forEach(Player::doneDealerDealtBlackjack);
    }

    private boolean haveMorePlayers() {
        return playerIterator.hasNext();
    }

    public void playerHits() {
        requireCardsDealt();
        currentPlayer.hit(shoe);
        playerStateChanged();
    }

    public void playerStands() {
        requireCardsDealt();
        currentPlayer.stand();
        playerStateChanged();
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

    public void placeBets(List<Integer> bets) {
        requireValidBetAmounts(bets);
        requireBetsMatchPlayerCount(bets);
        requireCardsNotDealt();

        this.bets = List.copyOf(bets);
    }

    private void requireValidBetAmounts(List<Integer> bets) {
        if (bets.stream().anyMatch(this::isInvalidBetAmount)) {
            throw new InvalidBetAmount();
        }
    }

    private boolean isInvalidBetAmount(Integer bet) {
        return bet <= 0 || bet > 100;
    }

    public List<Integer> currentBets() {
        return List.copyOf(bets);
    }

    private void requireBetsMatchPlayerCount(List<Integer> bets) {
        if (bets.size() != playerCount()) {
            throw new BetsNotMatchingPlayerCount();
        }
    }

    private void requireCardsDealt() {
        if (cardsDealt()) {
            throw new CardsNotDealt();
        }
    }

    private void requireCardsNotDealt() {
        if (!cardsDealt()) {
            throw new CannotPlaceBetsAfterInitialDeal();
        }
    }

    private boolean cardsDealt() {
        return currentPlayer.cards().isEmpty();
    }
}
