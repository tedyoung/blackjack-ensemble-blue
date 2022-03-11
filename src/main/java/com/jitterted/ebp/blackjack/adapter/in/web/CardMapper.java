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

    public static List<String> dealerInProgressCardsAsString(List<Card> cards) {
        List<String> dealerCards = cards.stream()
                                    .map(CardMapper::displayOf)
                                    .collect(Collectors.toList());
        // REFACTOR THIS, UGLY
        dealerCards.set(1, "XX");

        return dealerCards;

    }
}
