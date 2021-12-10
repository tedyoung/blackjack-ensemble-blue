package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Player;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerOutcomeViewTest {

    private static final Hand DUMMY_DEALER_HAND = null;

    @Test
    public void playerHasBlackjackThenDisplaysIdCardsAndOutcome() throws Exception {
        Deck deck = new StubDeck(Rank.KING, Rank.ACE);
        Player player = createPlayerWithInitialDeal(deck);

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView.of(DUMMY_DEALER_HAND, player);

        assertThat(playerOutcomeView.getPlayerId())
                .isEqualTo(1);
        assertThat(playerOutcomeView.getPlayerCards())
                .hasSize(2)
                .containsOnly("K♥", "A♥");
        assertThat(playerOutcomeView.getPlayerOutcome())
                .isEqualTo("BLACKJACK");
    }

    @Test
    public void dealerBustedAndPlayerNotBustedThenOutcomeIsDealerBusted() throws Exception {
        Hand dealerBustedHand = new Hand(List.of(new Card(Suit.CLUBS, Rank.TEN),
                                                 new Card(Suit.CLUBS, Rank.QUEEN),
                                                 new Card(Suit.CLUBS, Rank.TWO)
        ));
        Deck deck = new StubDeck(Rank.KING, Rank.EIGHT);
        Player player = createPlayerWithInitialDeal(deck);
        player.stand();

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView.of(dealerBustedHand, player);

        assertThat(playerOutcomeView.getPlayerOutcome())
                .isEqualTo("DEALER_BUSTED");
    }

    private Player createPlayerWithInitialDeal(Deck deck) {
        Player player = new Player(1);
        player.initialDrawFrom(deck);
        player.initialDrawFrom(deck);
        return player;
    }

}