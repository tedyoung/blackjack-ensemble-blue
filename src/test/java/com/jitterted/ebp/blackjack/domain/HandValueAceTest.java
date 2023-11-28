package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HandValueAceTest {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;

    @Test
    void handWithOneAceAndOtherCardValuedLessThan10ThenAceIsValuedAt11() throws Exception {
        Hand hand = createHand(Rank.ACE, Rank.FIVE);

        assertThat(hand.valueEquals(11 + 5))
                .isTrue();
    }

    @Test
    void handWithOneAceAndOtherCardsValuedAt10ThenAceIsValuedAt11() throws Exception {
        Hand hand = createHand(Rank.ACE, Rank.TEN);

        assertThat(hand.valueEquals(11 + 10))
                .isTrue();
    }

    @Test
    void handWithOneAceAndOtherCardsValuedAs11ThenAceIsValuedAt1() throws Exception {
        Hand hand = createHand(Rank.ACE, Rank.EIGHT, Rank.THREE);

        assertThat(hand.valueEquals(1 + 8 + 3))
                .isTrue();
    }

    private Hand createHand(Rank... ranks) {
        List<Card> cards = new ArrayList<>();
        for (Rank rank : ranks) {
            cards.add(new Card(DUMMY_SUIT, rank));
        }
        return new Hand(cards);
    }

}
