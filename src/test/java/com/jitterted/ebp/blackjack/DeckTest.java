package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DeckTest {

  @Test
  public void fullDeckHas52Cards() throws Exception {
    Deck deck = new Deck();

    assertThat(deck.size())
        .isEqualTo(52);
  }

  @Test
  public void drawCardFromDeckReducesDeckSizeByOne() throws Exception {
    Deck deck = new Deck();

    deck.draw();

    assertThat(deck.size())
        .isEqualTo(51);
  }

  @Test
  public void drawCardFromDeckReturnsValidCard() throws Exception {
    Deck deck = new Deck();

    Card card = deck.draw();

    assertThat(card)
        .isNotNull();

    assertThat(card.rankValue())
        .isGreaterThan(0);
  }

  @Test
  public void drawAllCardsResultsInSetOf52UniqueCards() throws Exception {
    Deck deck = new Deck();

    Set<Card> drawnCards = new HashSet<>();
    for (int i = 1; i <= 52; i++) {
      drawnCards.add(deck.draw());
    }

    assertThat(drawnCards)
        .hasSize(52);
  }

}