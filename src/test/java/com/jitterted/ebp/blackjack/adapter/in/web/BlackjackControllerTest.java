package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

    public static final Deck NULL_DUMMY_DECK = null;

    @Test
    public void startGameResultsInCardsDealtToPlayer() throws Exception {
        GameService gameService = new GameService(new Deck());
        BlackjackController blackjackController = new BlackjackController(gameService);

        String redirect = blackjackController.startGame(2, "");

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
        GameService gameService = new GameService(new Deck());
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(1, "");

        Model model = new ConcurrentModel();
        blackjackController.gameInProgressView(model);

        assertThat(model.getAttribute("gameInProgressView"))
                .isNotNull();
    }

    @Test
    public void hitCommandDealsAnotherCardToPlayer() throws Exception {
        GameService gameService = new GameService(NULL_DUMMY_DECK);
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck()
                                                       .convertToString();
        blackjackController.startGame(1, customDeck);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(3);
    }

    @Test
    public void hitAndPlayerGoesBustRedirectsToGameDonePage() throws Exception {
        GameService gameService = new GameService(NULL_DUMMY_DECK);
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit()
                                                       .convertToString();
        blackjackController.startGame(1, customDeck);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void donePageShowsFinalGameViewWithOutcome() throws Exception {
        GameService gameService = new GameService(NULL_DUMMY_DECK);
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck()
                                                       .convertToString();
        blackjackController.startGame(1, customDeck);
        blackjackController.standCommand();

        Model model = new ConcurrentModel();
        blackjackController.viewDone(model);

        assertThat(model.getAttribute("gameOutcomeView"))
                .isInstanceOf(GameOutcomeView.class);
    }

    @Test
    public void singlePlayerGameStandResultsInGameOver() throws Exception {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(1, "");

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/done");

        assertThat(gameService.currentGame().isGameOver())
                .isTrue();
    }

    @Test
    void twoPlayerGameFirstPlayerStandsGameInProgress() throws Exception {
        GameService gameService = new GameService(NULL_DUMMY_DECK);
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack()
                                                      .convertToString();
        blackjackController.startGame(2, customDeck);

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");
    }

    @Test
    public void givenTwoPlayersFirstPlayerGoesBustNextPlayerCanStand() throws Exception {
        GameService gameService = new GameService(NULL_DUMMY_DECK);
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = new StubDeck(Rank.EIGHT, Rank.NINE, Rank.ACE,
                                         Rank.JACK, Rank.TEN, Rank.FOUR,
                                         Rank.KING, Rank.SEVEN, Rank.SIX).convertToString();
        blackjackController.startGame(2, customDeck);
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
        GameService gameService = new GameService(NULL_DUMMY_DECK);
        BlackjackController blackjackController = new BlackjackController(gameService);

        blackjackController.startGame(1, "8,Q,K,2");

        assertThat(gameService.currentGame().currentPlayerCards())
                .extracting(Card::rank)
                .containsExactly(Rank.EIGHT, Rank.KING);
        assertThat(gameService.currentGame().dealerHand().cards())
                .extracting(Card::rank)
                .containsExactly(Rank.QUEEN, Rank.TWO);
    }

}
