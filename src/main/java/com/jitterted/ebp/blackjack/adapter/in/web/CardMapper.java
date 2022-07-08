package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class CardMapper {

    static List<String> cardsAsString(List<Card> cards) {
        return cards.stream()
                    .map(CardMapper::displayOf)
                    .collect(Collectors.toList());
    }

    public static List<String> dealerInProgressCardsAsString(List<Card> cards) {
        if (cards.size() == 0) {
            return Collections.emptyList();
        }

        List<String> dealerCards = cards.stream()
                                    .map(CardMapper::displayOf)
                                    .collect(Collectors.toList());
        return dealerCards;
    }

    private static String displayOf(Card card) {
        if (card.isFaceDown()) {
            return "XX";
        }

        return card.rank().display() + card.suit().displaySymbol();
    }
}