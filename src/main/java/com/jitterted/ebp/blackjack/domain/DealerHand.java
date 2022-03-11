package com.jitterted.ebp.blackjack.domain;

public class DealerHand extends Hand {

    boolean dealerMustDrawCard() {
        return value() <= 16;
    }
}
