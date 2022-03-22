package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.FaceUpCard;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HandDtoTest {
    @Test
    public void handConvertedToStringWithSlashSeparators() throws Exception {
        Hand hand = new Hand(List.of(new FaceUpCard(Suit.HEARTS, Rank.QUEEN),
                                     new FaceUpCard(Suit.HEARTS, Rank.EIGHT)));

        String handAsString = new HandDto(hand).asString();

        assertThat(handAsString)
                .isEqualTo("Q♥/8♥");
    }

    @Test
    void initializedWithHandWithCardsConvertedToStringWithSlashSeparators() {
        List<Card> cards = List.of(new FaceUpCard(Suit.HEARTS, Rank.QUEEN),
                                   new FaceUpCard(Suit.HEARTS, Rank.EIGHT));

        String handAsString = new HandDto(cards).asString();

        assertThat(handAsString)
                .isEqualTo("Q♥/8♥");
    }
}
