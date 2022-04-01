package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class StubDeckBuilder {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;
    public static final int INITIAL_DEAL_CARD_COUNT = 2;

    private final int playerCount;
    private final List<Card> cards;
    private final Deque<Rank> dealerRankQueue;
    private final List<Deque<Rank>> allPlayerRankQueues;

    private StubDeckBuilder(int playerCount) {
        this.playerCount = playerCount;
        cards = new ArrayList<>();
        allPlayerRankQueues = new ArrayList<>();
        dealerRankQueue = new ArrayDeque<>();
    }

    public static StubDeckBuilder playerCountOf(int playerCount) {
        return new StubDeckBuilder(playerCount);
    }

    public StubDeckBuilder addPlayerHitsOnceDoesNotBust() {
        return addPlayerWithRanks(Rank.QUEEN, Rank.SEVEN, Rank.THREE);
    }

    public StubDeckBuilder addPlayerDealtBlackjack() {
        return addPlayerWithRanks(Rank.QUEEN, Rank.ACE);
    }

    public StubDeckBuilder addPlayerHitsAndGoesBust() {
        return addPlayerWithRanks(Rank.JACK, Rank.NINE, Rank.FOUR);
    }

    public StubDeckBuilder addPlayerWithRanks(Rank... ranks) {
        Deque<Rank> playerRanks = new ArrayDeque<>();
        buildQueue(playerRanks, ranks);
        allPlayerRankQueues.add(playerRanks);
        return this;
    }

    public StubDeck buildWithDealerDoesNotDrawCards() {
        return buildWithDealerRanks(Rank.EIGHT, Rank.TEN);
    }

    public StubDeck buildWithDealerRanks(Rank... ranks) {
        buildQueue(dealerRankQueue, ranks);
        return build();
    }

    private StubDeck build() {
        requireAddedCorrectNumberOfPlayers();
        initialDeal();
        addRemainingCardsForAllPlayers();
        addRemainingCardsFrom(dealerRankQueue);
        return new StubDeck(cards);
    }

    private void addRemainingCardsFrom(Deque<Rank> queue) {
        while (!queue.isEmpty()) {
            addCardWithRank(queue.poll());
        }
    }

    // [  initial deal part ] [ player 1 ] [ player 2 ] [ ... ] [ dealer tail ]
    private void addRemainingCardsForAllPlayers() {
        for (Deque<Rank> playerRankQueue : allPlayerRankQueues) {
            addRemainingCardsFrom(playerRankQueue);
        }
    }

    private void requireAddedCorrectNumberOfPlayers() {
        if (allPlayerRankQueues.size() != playerCount) {
            throw new IllegalStateException("Player count mismatch");
        }
    }

    private void initialDeal() {
        for (int i = 0; i < INITIAL_DEAL_CARD_COUNT; i++) {
            addOneCardFromEachPlayer();
            addCardWithRank(dealerRankQueue.poll());
        }
    }

    private void addOneCardFromEachPlayer() {
        for (Deque<Rank> playerRankQueue : allPlayerRankQueues) {
            addCardWithRank(playerRankQueue.poll());
        }
    }

    private void buildQueue(Deque<Rank> queue, Rank... ranks) {
        for (Rank rank : ranks) {
            queue.offer(rank);
        }
    }

    private void addCardWithRank(Rank rank) {
        cards.add(new DefaultCard(DUMMY_SUIT, rank));
    }
}
