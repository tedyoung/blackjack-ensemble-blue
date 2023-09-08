package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameBuilder;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeViewTest {

    @Test
    public void twoPlayerGameAndGameIsOverThenHasTwoPlayerOutcomes() throws Exception {
        StubDeck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);
        game.playerStands();
        game.playerStands();

        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game);

        assertThat(gameOutcomeView.getPlayerOutcomes().get(0).getPlayerId())
                .isEqualTo(game.playerResults().get(0).id());
        assertThat(gameOutcomeView.getPlayerOutcomes().get(1).getPlayerId())
                .isEqualTo(game.playerResults().get(1).id());
    }

    @Test
    public void gameIsOverThenHasDealerCards() throws Exception {
        StubDeck deck = new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                                     Rank.EIGHT, Rank.QUEEN, Rank.NINE);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);
        game.playerStands();
        game.playerStands();

        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game);

        assertThat(gameOutcomeView.getDealerCards())
                .containsExactly("10♥", "9♥");
    }

}