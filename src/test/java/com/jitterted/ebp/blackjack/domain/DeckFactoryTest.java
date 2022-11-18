package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DeckFactoryTest {

    @Test
    void deckFactoryReturnsNewDeckUponCreateDeck() {
        DeckFactory deckFactory = new DeckFactory();

        Deck deck1 = deckFactory.createDeck();
        Deck deck2 = deckFactory.createDeck();

        assertThat(deck1)
                .isNotSameAs(deck2);
    }
}
