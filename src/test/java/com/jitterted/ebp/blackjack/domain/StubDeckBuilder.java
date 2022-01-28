package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class StubDeckBuilder {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;
    private List<Card> cards;

    public StubDeckBuilder playerHitsOnceDoesNotBust() {
        List<Rank> playerRanks = List.of(Rank.QUEEN,
                                         Rank.SEVEN,
                                         Rank.THREE);
        List<Rank> dealerRanks = List.of(Rank.EIGHT, Rank.FOUR);
        cards = new ArrayList<>();
        addCardWithRank(playerRanks.get(0));
        addCardWithRank(dealerRanks.get(0));
        addCardWithRank(playerRanks.get(1));
        addCardWithRank(dealerRanks.get(1));
        addCardWithRank(playerRanks.get(2));
        return this;
    }

    private boolean addCardWithRank(Rank rank) {
        return cards.add(new Card(DUMMY_SUIT, rank));
    }

    public StubDeck build() {
        return new StubDeck(cards);
    }
}
