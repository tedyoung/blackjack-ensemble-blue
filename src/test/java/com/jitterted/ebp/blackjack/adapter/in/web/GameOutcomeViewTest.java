package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeViewTest {

    @Test
    public void twoPlayerGameWhenGameIsOverHasTwoPlayerOutcomes() throws Exception {
        // GameOutcomeView
            // List<String> dealerCards
            // List<PlayerOutcomeView>

        // DealerCards = K, J, ...
        // Player1     = 1H, 3C, ..., BUSTED
        // Player2     = ... , BLACKJACK

        StubDeck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        Game game = new Game(deck, 2);
        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game);

        assertThat(gameOutcomeView.getPlayerOutcomes())
                .hasSize(2);

    }
}