package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CardTest {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;

    @Test
    public void withNumberCardHasNumericValueOfTheNumber() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.SEVEN);

        assertThat(card.rankValue())
                .isEqualTo(7);
    }

    @Test
    public void withValueOfQueenHasNumericValueOf10() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.QUEEN);

        assertThat(card.rankValue())
                .isEqualTo(10);
    }

    @Test
    public void withAceHasNumericValueOf1() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.ACE);

        assertThat(card.rankValue())
                .isEqualTo(1);
    }

    @Test
    public void newlyCreatedCardIsFaceUp() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.ACE);

        assertThat(card.isFaceDown())
                .isFalse();
    }

    @Test
    public void flipFaceUpCardIsFaceDown() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.ACE);

        card.flip();

        assertThat(card.isFaceDown())
                .isTrue();
    }

    @Test
    public void flipFaceDownCardIsFaceUp() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.ACE);
        card.flip();

        card.flip();

        assertThat(card.isFaceDown())
                .isFalse();
    }
}