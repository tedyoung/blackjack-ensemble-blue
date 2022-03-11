package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class DealerHand extends Hand {

    boolean dealerMustDrawCard() {
        return value() <= 16;
    }

    @Override
    // As a Query method, this must return a SNAPSHOT, not a live "View"
    public List<Card> cards() {
        return List.copyOf(cards);
    }
}
