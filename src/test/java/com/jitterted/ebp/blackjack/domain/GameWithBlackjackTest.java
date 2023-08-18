package com.jitterted.ebp.blackjack.domain;

import dev.ted.junit.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayNameGeneration(ReplaceCamelCase.class)
class GameWithBlackjackTest {

    @Test
    void givenPlayerDealtBlackjackWhenPlayerHitsThenThrowsGameAlreadyOverException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(playerDrawsBlackjackDeck);

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(GameAlreadyOver.class);
    }

    @Test
    void givenPlayerDealtBlackjackWhenPlayerStandsThrowsGameAlreadyOverException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(playerDrawsBlackjackDeck);

        assertThatThrownBy(game::playerStands)
                .isInstanceOf(GameAlreadyOver.class);
    }

    @Test
    public void givenSinglePlayerDealtBlackjackThenResultHasBlackjackOutcome() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void whenDealerDealtBlackjackDealerWins() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .buildWithDealerDealtBlackjack();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_LOSES);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.DEALER_DEALT_BLACKJACK);
    }

    @Test
    public void whenDealerDealtBlackjackAndTwoPlayersNotDealtBlackjackDealerWins() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                   .buildWithDealerDealtBlackjack();
        Game game = GameFactory.createTwoPlayerGamePlaceBets(deck, new PlayerId(44), new PlayerId(17));

        game.initialDeal();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_LOSES, PlayerOutcome.PLAYER_LOSES);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.DEALER_DEALT_BLACKJACK, PlayerReasonDone.DEALER_DEALT_BLACKJACK);
    }

    @Test
    public void bothDealerAndPlayerDealtBlackjackResultIsPushes() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerDealtBlackjack()
                                   .buildWithDealerDealtBlackjack();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_PUSHES_DEALER);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.DEALER_DEALT_BLACKJACK);
    }

    @Test
    public void playerBlackjackHasHigherPriorityThanPush() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.SEVEN);
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.BLACKJACK);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.PLAYER_HAS_BLACKJACK);
    }

    @Test
    public void onePlayerDealtBlackjackDealerDoesNotHaveBlackjackDealerDoesNotTakeTurn() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.SEVEN);
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
}

