package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeTest {

  @Test
  public void playerGoesBustResultsInPlayerLoses() throws Exception {
    Deck playerHitsGoesBustDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                               Rank.TEN, Rank.FOUR,
                                               Rank.THREE);
    Game game = new Game(playerHitsGoesBustDeck);

    game.initialDeal();

    game.playerHits();

    assertThat(game.determineOutcome())
        .isEqualByComparingTo(GameOutcome.PLAYER_BUSTED);
  }

  @Test
  public void playerBeatsDealer() throws Exception {
    Deck playerBeatsDealerDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                              Rank.TEN, Rank.JACK);
    Game game = new Game(playerBeatsDealerDeck);
    game.initialDeal();

    game.playerStands();
    game.dealerTurn();

    assertThat(game.determineOutcome())
        .isEqualByComparingTo(GameOutcome.PLAYER_BEATS_DEALER);
  }


}