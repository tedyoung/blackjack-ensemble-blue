package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;

import java.util.List;
import java.util.stream.Collectors;

class CardMapper {

    static List<String> cardsAsString(List<Card> cards) {
        return cards.stream()
                    .map(CardMapper::displayOf)
                    .collect(Collectors.toList());
    }

    private static String displayOf(Card card) {
        return card.rank().display() + card.suit().symbol();
    }
}
