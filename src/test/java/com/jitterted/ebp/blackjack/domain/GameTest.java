package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameTest {

  @Test
  public void givenPlayerDealtBlackjackWhenPlayerHitsThenThrowsException() throws Exception {
    Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
    Game game = new Game(playerDrawsBlackjackDeck);
    game.initialDeal();

    assertThatThrownBy(() -> {
      game.playerHits();
    }).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void givenPlayerDealtBlackjackWhenPlayerStandsThrowsException() throws Exception{
    Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
    Game game = new Game(playerDrawsBlackjackDeck);
    game.initialDeal();

    assertThatThrownBy(() -> {
      game.playerStands();
    }).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void givenPlayerBustsWhenPlayerHitsThenThrowsException() throws Exception {
    Deck playerBustsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                        Rank.TEN, Rank.FOUR,
                                        Rank.THREE,
                                        Rank.TEN);
    Game game = new Game(playerBustsDeck);
    game.initialDeal();
    game.playerHits();

    assertThatThrownBy(() -> {
     game.playerHits();
    }).isInstanceOf(IllegalStateException.class);
  }
}
