package com.jitterted.ebp.blackjack.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class FaceDownCardTest {
    @Test
    public void cardDecoratesAsFaceDown() throws Exception {
        Card card = new FaceUpCard(Suit.HEARTS, Rank.TEN);

        FaceDownCard faceDownCard = new FaceDownCard(card);

        assertThat(faceDownCard.isFaceDown())
                .isTrue();
    }

    @Test
    public void faceDownCardPreservesRankSuitAndRankValue() throws Exception {
        Card card = new FaceUpCard(Suit.HEARTS, Rank.TEN);

        FaceDownCard faceDownCard = new FaceDownCard(card);

        assertThat(faceDownCard.suit())
                .isEqualTo(Suit.HEARTS);
        assertThat(faceDownCard.rank())
                .isEqualTo(Rank.TEN);
        assertThat(faceDownCard.rankValue())
                .isEqualTo(10);
    }

}
