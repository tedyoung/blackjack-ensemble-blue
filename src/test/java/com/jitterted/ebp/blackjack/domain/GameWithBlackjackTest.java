package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameWithBlackjackTest {

    @Test
    void givenPlayerDealtBlackjackWhenPlayerHitsThenThrowsException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        final DeckFactory deckFactory = new DeckFactory(playerDrawsBlackjackDeck);
        Game game = new Game(1, new Shoe(deckFactory));
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.playerHits();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenPlayerDealtBlackjackWhenPlayerStandsThrowsException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        final DeckFactory deckFactory = new DeckFactory(playerDrawsBlackjackDeck);
        Game game = new Game(1, new Shoe(deckFactory));
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.playerStands();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void givenSinglePlayerDealtBlackjackThenResultHasBlackjackOutcome() {
        final DeckFactory deckFactory = new DeckFactory(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        Game game = new Game(
                1, new Shoe(deckFactory));
        game.initialDeal();

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void whenDealerDealtBlackjackDealerWins() throws Exception {
        final DeckFactory deckFactory = new DeckFactory(StubDeckBuilder.playerCountOf(1)
                                                                       .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                                                       .buildWithDealerDealtBlackjack());
        Game game = new Game(1, new Shoe(deckFactory));

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
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerDealtBlackjack();
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));

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
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerDealtBlackjack();
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(1, new Shoe(deckFactory));

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
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerDealtBlackjack()
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.SEVEN);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));

        game.initialDeal();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.BLACKJACK, PlayerOutcome.PLAYER_LOSES);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.PLAYER_HAS_BLACKJACK, PlayerReasonDone.PLAYER_STANDS);
    }

    @Test
    public void allPlayersDealtBlackjackDealerDoesNotHaveBlackjackDealerDoesNotTakeTurn() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.SEVEN);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(1, new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
}