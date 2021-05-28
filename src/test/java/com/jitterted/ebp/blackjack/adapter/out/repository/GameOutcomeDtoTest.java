package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameOutcome;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameOutcomeDtoTest {

  @Test
  public void playerStandsGameIsOverAsString() throws Exception {
    Game game = new Game(new StubDeck(List.of(new Card(Suit.HEARTS, Rank.QUEEN), new Card(Suit.HEARTS, Rank.THREE),
                                              new Card(Suit.HEARTS, Rank.EIGHT), new Card(Suit.HEARTS, Rank.FIVE),
                                              new Card(Suit.HEARTS, Rank.JACK))));
    game.initialDeal();
    game.playerStands();

    GameOutcomeDto gameOutcomeDto = new GameOutcomeDto(game);
    String gameAsString = gameOutcomeDto.asString();
    assertThat(gameAsString).isEqualTo("Q♥/8♥,3♥/5♥/J♥," + GameOutcome.PLAYER_PUSHES_DEALER);
  }

  @Test
  public void playerStandsGameIsOverAsStringReturnsPlayerAndDealerOutcome() throws Exception {
    Game game = new Game(new StubDeck(List.of(new Card(Suit.HEARTS, Rank.QUEEN), new Card(Suit.HEARTS, Rank.FOUR),
                                              new Card(Suit.HEARTS, Rank.EIGHT), new Card(Suit.HEARTS, Rank.FIVE), new Card(Suit.HEARTS, Rank.JACK))));
    game.initialDeal();
    game.playerStands();
    GameOutcomeDto gameOutcomeDto = new GameOutcomeDto(game);

    String gameAsString = gameOutcomeDto.asString();
    assertThat(gameAsString)
        .isEqualTo("Q♥/8♥,4♥/5♥/J♥," + GameOutcome.PLAYER_LOSES);
  }

  @Test
  public void playerStandsGameIsOverAsStringReturnsPlayerAndDealerWithMixedSuit() throws Exception {
    Game game = new Game(new StubDeck(List.of(new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.CLUBS, Rank.FOUR),
                                              new Card(Suit.DIAMONDS, Rank.EIGHT), new Card(Suit.HEARTS, Rank.FIVE),
                                              new Card(Suit.HEARTS, Rank.JACK))));
    game.initialDeal();
    game.playerStands();
    GameOutcomeDto gameOutcomeDto = new GameOutcomeDto(game);

    String gameAsString = gameOutcomeDto.asString();

    assertThat(gameAsString)
        .isEqualTo("Q♠/8♦,4♣/5♥/J♥," + GameOutcome.PLAYER_LOSES);
  }
}
