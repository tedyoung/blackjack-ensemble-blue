package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

class StubDeck extends Deck {
  private static final Suit DUMMY_SUIT = Suit.HEARTS;
  private final ListIterator<Card> iterator;

  public StubDeck(Rank... ranks) {
    List<Card> cards = new ArrayList<>();
    for (Rank rank : ranks) {
      cards.add(new Card(DUMMY_SUIT, rank));
    }
    this.iterator = cards.listIterator();
  }

  @Override
  public Card draw() {
    return iterator.next();
  }
}
