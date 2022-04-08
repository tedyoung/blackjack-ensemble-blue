package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.fusesource.jansi.Ansi.ansi;

class CardDisplayTest {
    private static final Rank DUMMY_RANK = Rank.TEN;

    @Test
    public void displayAsTenCard() throws Exception {
        Card card = new Card(Suit.HEARTS, Rank.TEN);

        assertThat(ConsoleCard.display(card))
                .isEqualTo("[31m┌─────────┐[1B[11D│10       │[1B[11D│         │[1B[11D│    ♥    │[1B[11D│         │[1B[11D│       10│[1B[11D└─────────┘");
    }

    @Test
    public void displayNonTenCard() throws Exception {
        Card card = new Card(Suit.CLUBS, Rank.EIGHT);

        assertThat(ConsoleCard.display(card))
                .isEqualTo("[30m┌─────────┐[1B[11D│8        │[1B[11D│         │[1B[11D│    ♣    │[1B[11D│         │[1B[11D│        8│[1B[11D└─────────┘");
    }

    @Test
    public void suitOfHeartsOrDiamondsIsDisplayedInRed() throws Exception {
        // given a card with Hearts or Diamonds
        Card heartsCard = new Card(Suit.HEARTS, DUMMY_RANK);
        Card diamondsCard = new Card(Suit.DIAMONDS, DUMMY_RANK);

        // when we ask for its display representation
        String ansiRedString = ansi().fgRed().toString();

        // then we expect a red color ansi sequence
        assertThat(ConsoleCard.display(heartsCard))
                .contains(ansiRedString);
        assertThat(ConsoleCard.display(diamondsCard))
                .contains(ansiRedString);
    }

}
