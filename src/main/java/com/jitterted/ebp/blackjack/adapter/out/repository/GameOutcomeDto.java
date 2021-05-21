package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Hand;

import java.util.stream.Collectors;

public class GameOutcomeDto {

    private Hand playerCards;

    public GameOutcomeDto(Hand playerCards) {
        this.playerCards = playerCards;
    }

    public String asString() {
        return playerCards.cards().stream().map(card -> card.rank().display() + card.suit().symbol()).collect(Collectors.joining("/"));
    }
}
