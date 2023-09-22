package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameBuilder;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameResultDtoTest {

    @Test
    public void playerStandsGameIsOverAsStringReturnsPlayerAndDealerWithMixedSuit() throws Exception {
        final StubDeck stubDeck = new StubDeck(List.of(new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.CLUBS, Rank.FOUR),
                                                       new Card(Suit.DIAMONDS, Rank.EIGHT), new Card(Suit.HEARTS, Rank.FIVE),
                                                       new Card(Suit.HEARTS, Rank.JACK)));
        Game game = GameBuilder.playerCountOf(1)
                          .deck(stubDeck)
                          .addPlayer(new PlayerId(27), Bet.of(10))
                          .initialDeal()
                          .build();
        game.playerStands();
        GameResultDto gameResultDto = new GameResultDto(game);

        String gameAsString = gameResultDto.asString();

        assertThat(gameAsString)
                .isEqualTo("Q♠/8♦,4♣/5♥/J♥,Player Loses");
    }
}
