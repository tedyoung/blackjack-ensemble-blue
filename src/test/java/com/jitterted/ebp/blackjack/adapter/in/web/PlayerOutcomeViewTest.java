package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Player;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import org.junit.jupiter.api.Test;

class PlayerOutcomeViewTest {

    @Test
    public void playerHasBlackjackThenDisplaysIdCardsAndOutcome() throws Exception {
        // PlayerOutcomeView
            // PlayerId
            // List<String> playerCards
            // PlayerOutcome

        Deck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Player player = new Player();
        player.initialDrawFrom(deck);

        PlayerOutcomeView playerOutcomeView = new PlayerOutcomeView(player);

        assertThat(playerOutcomeView.getPlayerId())
                .isEqualTo(1);
        assertThat(playerOutcomeView.getPlayerCards())
                .hasSize(2);
        assertThat(playerOutcomeView.getPlayerOutcome())
                .isEqualTo("BLACKJACK");
    }
}