package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class StubDeckBuilder {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;
    public static final int INITIAL_DEAL_CARD_COUNT = 2;

    private final int playerCount;
    private List<Card> cards;
    private List<Rank> dealerRanks;
    private final List<Deque<Rank>> allPlayerRanks;

    private StubDeckBuilder(int playerCount) {
        this.playerCount = playerCount;
        allPlayerRanks = new ArrayList<>();
    }

    public static StubDeckBuilder playerCountOf(int playerCount) {
        return new StubDeckBuilder(playerCount);
    }

    public StubDeckBuilder addPlayerHitsOnceDoesNotBust() {
        addPlayerWithRanks(Rank.QUEEN, Rank.SEVEN, Rank.THREE);
        return this;
    }

    public StubDeckBuilder addPlayerDealtBlackjack() {
        addPlayerWithRanks(Rank.QUEEN, Rank.ACE);
        return this;
    }

    public StubDeckBuilder addPlayerHitsAndGoesBust() {
        addPlayerWithRanks(Rank.JACK, Rank.NINE, Rank.FOUR);
        return this;
    }

    public void addPlayerWithRanks(Rank... ranks) {
        Deque<Rank> playerRanks = new ArrayDeque<>();
        for (Rank rank : ranks) {
            playerRanks.offer(rank);
        }
        allPlayerRanks.add(playerRanks);
    }

    public StubDeck buildWithDealerDoesNotDrawCards() {
        dealerRanks = List.of(Rank.EIGHT, Rank.TEN);
        return build();
    }

    private StubDeck build() {
        requireAddedCorrectNumberOfPlayers();
        cards = new ArrayList<>();
        initialDeal();
        addCardsForAllPlayers();
        return new StubDeck(cards);
    }

    private void addCardsForAllPlayers() {
        for (Deque<Rank> playerRankQueue : allPlayerRanks) {
            if (!playerRankQueue.isEmpty()) {
                addCardWithRank(playerRankQueue.poll());
            }
        }
    }

    private void requireAddedCorrectNumberOfPlayers() {
        if (allPlayerRanks.size() != playerCount) {
            throw new IllegalStateException("Player count mismatch");
        }
    }

    private void initialDeal() {
        for (int i = 0; i < INITIAL_DEAL_CARD_COUNT; i++) {
            for (Deque<Rank> playerRankQueue : allPlayerRanks) {
                addCardWithRank(playerRankQueue.poll());
            }
            addCardWithRank(dealerRanks.get(i));
        }
    }

    private void addCardWithRank(Rank rank) {
        cards.add(new Card(DUMMY_SUIT, rank));
    }
}
