package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerOutcomeViewTest {

    @Test
    public void playerHasBlackjackThenDisplaysIdCardsAndOutcome() throws Exception {
        Deck deck = new StubDeck(Rank.KING, Rank.ACE);
        PlayerInGame player = createPlayerWithInitialDeal(deck);
        PlayerResult playerResult = new PlayerResult(player,
                                                     PlayerOutcome.BLACKJACK,
                                                     Bet.of(20));

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView.of(playerResult);

        assertThat(playerOutcomeView.getPlayerId())
                .isEqualTo(1);
        assertThat(playerOutcomeView.getPlayerCards())
                .hasSize(2)
                .containsOnly("K♥", "A♥");
        assertThat(playerOutcomeView.getPlayerOutcome())
                .isEqualTo("BLACKJACK");
        assertThat(playerOutcomeView.getBetOutcome())
                .isEqualTo("You bet 20 and got back 50");
    }

    @Test
    public void playerHasLostThenDisplaysBetOutcome() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.TEN, Rank.TEN)
                                   .buildWithDealerDealtBlackjack();
        PlayerInGame player = createPlayerWithInitialDeal(deck);
        PlayerResult playerResult = new PlayerResult(player,
                                                     PlayerOutcome.PLAYER_LOSES,
                                                     Bet.of(25));

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView.of(playerResult);

        assertThat(playerOutcomeView.getBetOutcome())
                .isEqualTo("You bet 25 and got back 0");
    }

    private PlayerInGame createPlayerWithInitialDeal(Deck deck) {
        PlayerInGame player = new PlayerInGame(PlayerId.of(1));
        Shoe shoe = new Shoe(List.of(deck));
        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);
        return player;
    }
}