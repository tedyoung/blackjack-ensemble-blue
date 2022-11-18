package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Hand;

import java.util.List;
import java.util.stream.Collectors;

public class HandDto {

    private final List<Card> cards;

    public HandDto(Hand hand) {
        this(hand.cards());
    }

    public HandDto(List<Card> cards) {
        this.cards = cards;
    }

    public String asString() {
        return cards.stream()
                    .map(card -> card.rank().display()
                            + card.suit().displaySymbol())
                    .collect(Collectors.joining("/"));
    }
}
