package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ShuffledDeckTest {

    @Test
    void givenListOfOneIndexReturnsThreeOfHearts() {
        List<Integer> cardNumbers = List.of(2);

        ShuffledDeck deck = new ShuffledDeck(cardNumbers);

        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.THREE));
    }

    @Test
    void givenListOfTwoIndexesReturnsCardsWithIndexes() {
        List<Integer> cardNumbers = List.of(2, 1);

        ShuffledDeck deck = new ShuffledDeck(cardNumbers);

        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.THREE));
        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.TWO));
    }

    @Test
    void givenAMultipleListOfIndexesRearrangesCardsBasedOnTheNumbers() {
        List<Integer> cardNumbers = new ArrayList<>();
        for (int i = 51; i >= 0; i--) {
            cardNumbers.add(i);
        }

        ShuffledDeck deck = new ShuffledDeck(cardNumbers);
        for (int i = 0; i <= 50; i++) {
            deck.draw();
        }

        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.ACE));
    }

    @Test
    public void givenOutOfRangeIndexThrowsException() throws Exception {
        List<Integer> cardNumbers = List.of(52);

        assertThatThrownBy(() -> new ShuffledDeck(cardNumbers))
                .isInstanceOf(IllegalArgumentException.class);
    }
}