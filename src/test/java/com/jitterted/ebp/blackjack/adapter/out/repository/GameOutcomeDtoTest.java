package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeDtoTest {
    @Test
    public void handConvertedToStringWithSlashSeparators() throws Exception {
        Game game = new Game();
        Hand hand = new Hand(List.of(new Card(Suit.HEARTS, Rank.ACE),
                                     new Card(Suit.HEARTS, Rank.KING)));

        String handAsString = new GameOutcomeDto(hand).asString();

        assertThat(handAsString)
            .isEqualTo("A♥/K♥");
    }
}
