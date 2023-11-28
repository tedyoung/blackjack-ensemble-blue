package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class DeckTest {

    @Test
    void fullDeckHas52Cards() throws Exception {
        Deck deck = new OrderedDeck();

        assertThat(deck.size())
                .isEqualTo(52);
    }

    @Test
    void drawCardFromDeckReducesDeckSizeByOne() throws Exception {
        Deck deck = new OrderedDeck();

        deck.draw();

        assertThat(deck.size())
                .isEqualTo(51);
    }

    @Test
    void drawCardFromDeckReturnsValidCard() throws Exception {
        Deck deck = new OrderedDeck();

        Card card = deck.draw();

        assertThat(card)
                .isNotNull();

        assertThat(card.rankValue())
                .isGreaterThan(0);
    }

    @Test
    void drawAllCardsResultsInSetOf52UniqueCards() throws Exception {
        Deck deck = new OrderedDeck();

        Set<Card> drawnCards = new HashSet<>();
        for (int i = 1; i <= 52; i++) {
            drawnCards.add(deck.draw());
        }

        assertThat(drawnCards)
                .hasSize(52);
    }

}