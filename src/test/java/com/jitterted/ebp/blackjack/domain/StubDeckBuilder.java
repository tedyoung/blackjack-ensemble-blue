package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class StubDeckBuilder {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;

    private final int playerCount;
    private List<Card> cards;
    private List<Rank> dealerRanks;
    private Deque<Rank> playerRanks;
    private List<Deque<Rank>> allPlayerRanks;

    private StubDeckBuilder(int playerCount) {
        this.playerCount = playerCount;
        allPlayerRanks = new ArrayList<>();
    }

    public static StubDeckBuilder playerCountOf(int playerCount) {
        return new StubDeckBuilder(playerCount);
    }

    public StubDeckBuilder addPlayerHitsOnceDoesNotBust() {
        Deque<Rank> playerRanks = new ArrayDeque<>();
        playerRanks.offer(Rank.QUEEN);
        playerRanks.offer(Rank.SEVEN);
        playerRanks.offer(Rank.THREE);
        allPlayerRanks.add(playerRanks);
        return this;
    }

    public StubDeckBuilder addPlayerDealtBlackjack() {
        Deque<Rank> playerRanks = new ArrayDeque<>();
        playerRanks.offer(Rank.QUEEN);
        playerRanks.offer(Rank.ACE);
        allPlayerRanks.add(playerRanks);
        return this;
    }

    public StubDeckBuilder addPlayerHitsAndGoesBust() {
        return this;
    }

    public StubDeckBuilder withDealerDoesNotDrawCards() {
        dealerRanks = List.of(Rank.EIGHT, Rank.TEN);
        return this;
    }

    public StubDeck build() {
        cards = new ArrayList<>();
        createForInitialDeal();
        for (Deque<Rank> playerRankQueue : allPlayerRanks) {
            if (!playerRankQueue.isEmpty()) {
                addCardWithRank(playerRankQueue.poll());
            }
        }
        return new StubDeck(cards);
    }

    private void createForInitialDeal() {
        for (int i = 0; i < 2; i++) {
            for (Deque<Rank> playerRankQueue : allPlayerRanks) {
                addCardWithRank(playerRankQueue.poll());
            }
            addCardWithRank(dealerRanks.get(i));
        }
    }

    private boolean addCardWithRank(Rank rank) {
        return cards.add(new Card(DUMMY_SUIT, rank));
    }
}
