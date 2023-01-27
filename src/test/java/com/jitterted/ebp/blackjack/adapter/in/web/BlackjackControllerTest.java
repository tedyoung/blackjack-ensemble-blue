package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

    @Test
    public void createGameEndpointCreatesGameAndRedirectsToPlacedBets() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);

        String redirect = blackjackController.createGame(1, "");

        assertThat(redirect)
                .isEqualTo("redirect:/place-bets");
    }

    @Test
    @Disabled
    public void startGameResultsInCardsDealtToPlayer() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerHitsAndGoesBust()
                                       .addPlayerHitsAndGoesBust()
                                       .buildWithDealerDoesNotDrawCards();
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);

        String redirect = blackjackController.createGame(2, deck.convertToString());

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame())
                .isNotNull();
        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(2);
        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }

    @Test
    public void gameViewPopulatesViewModel() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerHitsAndGoesBust()
                                       .buildWithDealerDoesNotDrawCards();
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.createGame(1, deck.convertToString());

        Model model = new ConcurrentModel();
        blackjackController.gameInProgressView(model);

        assertThat(model.getAttribute("gameInProgressView"))
                .isNotNull();
    }

    @Test
    public void hitCommandDealsAnotherCardToPlayer() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck()
                                                       .convertToString();
        blackjackController.createGame(1, customDeck);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(3);
    }

    /**
     * @throws Exception
     */
    @Test
    public void hitAndPlayerGoesBustRedirectsToGameDonePage() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit()
                                                       .convertToString();
        blackjackController.createGame(1, customDeck);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void donePageShowsFinalGameViewWithOutcome() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck()
                                                       .convertToString();
        blackjackController.createGame(1, customDeck);
        blackjackController.standCommand();

        Model model = new ConcurrentModel();
        blackjackController.viewDone(model);

        assertThat(model.getAttribute("gameOutcomeView"))
                .isInstanceOf(GameOutcomeView.class);
    }

    @Test
    public void singlePlayerGameStandResultsInGameOver() throws Exception {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.createGame(1, deck.convertToString());

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/done");

        assertThat(gameService.currentGame().isGameOver())
                .isTrue();
    }

    @Test
    void twoPlayerGameFirstPlayerStandsGameInProgress() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack()
                                                      .convertToString();
        blackjackController.createGame(2, customDeck);

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");
    }

    @Test
    public void givenTwoPlayersFirstPlayerGoesBustNextPlayerCanStand() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = new StubDeck(Rank.EIGHT, Rank.NINE, Rank.ACE,
                                         Rank.JACK, Rank.TEN, Rank.FOUR,
                                         Rank.KING, Rank.SEVEN, Rank.SIX).convertToString();
        blackjackController.createGame(2, customDeck);
        blackjackController.hitCommand(); // first player is busted

        assertThat(gameService.currentGame().isGameOver())
                .isFalse();

        String page = blackjackController.standCommand(); // second player stands

        assertThat(page)
                .isEqualTo("redirect:/done");

        assertThat(gameService.currentGame().currentPlayerId())
                .isEqualTo(1);
        assertThat(gameService.currentGame().isGameOver())
                .isTrue();
    }

    @Test
    public void startGameUsesCustomDeck() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);

        blackjackController.createGame(1, "8,Q,K,2");

        assertThat(gameService.currentGame().currentPlayerCards())
                .extracting(Card::rank)
                .containsExactly(Rank.EIGHT, Rank.KING);
        assertThat(gameService.currentGame().dealerHand().cards())
                .extracting(Card::rank)
                .containsExactly(Rank.QUEEN, Rank.TWO);
    }

    @Test
    public void singlePlayerDealtBlackjackResultsInGameOver() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.createGame(1, "A,K,Q,7");

        String page = blackjackController.placeBets(new BettingForm(List.of(1)));

        assertThat(page)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void placeBetsPageRedirectsToStart() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);

        BettingForm bettingForm = new BettingForm(List.of(2));
        String page = blackjackController.placeBets(bettingForm);

        assertThat(gameService.currentBets())
                .containsExactly(2);
        assertThat(page)
                .isEqualTo("redirect:/game");

    }
}
