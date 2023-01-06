package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Player;
import com.jitterted.ebp.blackjack.domain.PlayerOutcome;
import com.jitterted.ebp.blackjack.domain.PlayerResult;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerOutcomeViewTest {

    @Test
    public void playerHasBlackjackThenDisplaysIdCardsAndOutcome() throws Exception {
        Deck deck = new StubDeck(Rank.KING, Rank.ACE);
        Player player = createPlayerWithInitialDeal(deck);
        PlayerResult playerResult = new PlayerResult(player, PlayerOutcome.BLACKJACK);

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView.of(playerResult);

        assertThat(playerOutcomeView.getPlayerId())
                .isEqualTo(1);
        assertThat(playerOutcomeView.getPlayerCards())
                .hasSize(2)
                .containsOnly("K♥", "A♥");
        assertThat(playerOutcomeView.getPlayerOutcome())
                .isEqualTo("BLACKJACK");
    }

    private Player createPlayerWithInitialDeal(Deck deck) {
        Player player = new Player(1);
        Shoe shoe = new Shoe(List.of(deck));
        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);
        return player;
    }
}