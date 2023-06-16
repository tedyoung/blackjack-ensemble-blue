package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        Game game = GameFactory.createOnePlayerGamePlaceBets(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }

    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        Game game = GameFactory.createOnePlayerGamePlaceBets(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_HAS_BLACKJACK));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck);
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck);
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_BUSTED));
    }

    @Test
    public void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        List<Deck> deckFactory = List.of(MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack());
        Shoe shoe = new Shoe(deckFactory);
        PlayerCount playerCount = PlayerCount.of(2);
        Game game = new Game(shoe, playerCount);
        List<Bet> bets = List.of(Bet.of(22), Bet.of(42));
        game.placeBets(bets);
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        PlayerDoneEvent player1event = new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS);
        PlayerDoneEvent player2event = new PlayerDoneEvent(1, PlayerReasonDone.PLAYER_STANDS);
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}
