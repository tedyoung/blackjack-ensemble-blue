package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FaceUpCardTest {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;

    @Test
    public void withNumberCardHasNumericValueOfTheNumber() throws Exception {
        Card card = new FaceUpCard(DUMMY_SUIT, Rank.SEVEN);

        assertThat(card.rankValue())
                .isEqualTo(7);
    }

    @Test
    public void withValueOfQueenHasNumericValueOf10() throws Exception {
        Card card = new FaceUpCard(DUMMY_SUIT, Rank.QUEEN);

        assertThat(card.rankValue())
                .isEqualTo(10);
    }

    @Test
    public void withAceHasNumericValueOf1() throws Exception {
        Card card = new FaceUpCard(DUMMY_SUIT, Rank.ACE);

        assertThat(card.rankValue())
                .isEqualTo(1);
    }

    @Test
    public void isFaceUp() throws Exception {
        FaceUpCard card = new FaceUpCard(DUMMY_SUIT, Rank.ACE);

        assertThat(card.isFaceDown())
                .isFalse();
    }

}