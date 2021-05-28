package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Hand;

import java.util.stream.Collectors;

public class HandDto {

    private Hand hand;

    public HandDto(Hand hand) {
        this.hand = hand;
    }

    public String asString() {
        return hand.cards().stream()
                   .map(card -> card.rank().display()
                           + card.suit().symbol())
                   .collect(Collectors.joining("/"));
    }
}
