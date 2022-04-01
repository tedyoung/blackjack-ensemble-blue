package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.DefaultCard;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameResultDtoTest {

    @Test
    public void playerStandsGameIsOverAsStringReturnsPlayerAndDealerWithMixedSuit() throws Exception {
        Game game = new Game(new StubDeck(List.of(new DefaultCard(Suit.SPADES, Rank.QUEEN), new DefaultCard(Suit.CLUBS, Rank.FOUR),
                                                  new DefaultCard(Suit.DIAMONDS, Rank.EIGHT), new DefaultCard(Suit.HEARTS, Rank.FIVE),
                                                  new DefaultCard(Suit.HEARTS, Rank.JACK))));
        game.initialDeal();
        game.playerStands();
        GameResultDto gameResultDto = new GameResultDto(game);

        String gameAsString = gameResultDto.asString();

        assertThat(gameAsString)
                .isEqualTo("Q♠/8♦,4♣/5♥/J♥,Player Loses");
    }
}
