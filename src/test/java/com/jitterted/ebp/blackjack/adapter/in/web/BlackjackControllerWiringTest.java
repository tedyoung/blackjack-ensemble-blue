package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.GameService;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerWiringTest {

    @Test
    public void startGameResultsInCardsDealtToPlayer() throws Exception {
        GameService gameService = new GameService();
        BlackjackController blackjackController = new BlackjackController(gameService);

        String redirect = blackjackController.startGame(2);

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
        GameService gameService = new GameService();
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(1);

        Model model = new ConcurrentModel();
        blackjackController.gameView(model);

        assertThat(model.getAttribute("gameInProgressView"))
                .isNotNull();
    }

    @Test
    public void hitCommandDealsAnotherCardToPlayer() throws Exception {
        GameService gameService = new GameService(SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck());
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(1);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(3);
    }

    @Test
    public void hitAndPlayerGoesBustRedirectsToGameDonePage() throws Exception {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(1);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void donePageShowsFinalGameViewWithOutcome() throws Exception {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(1);
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
        blackjackController.startGame(1);

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/done");

        assertThat(gameService.currentGame().isGameOver())
                .isTrue();
    }

    @Test
    void twoPlayerGameFirstPlayerStandsGameInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(2);

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");
    }

    @Test
    public void givenTwoPlayersFirstPlayerGoesBustNextPlayerCanStand() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.EIGHT, Rank.NINE,  Rank.ACE,
                                            Rank.JACK,  Rank.TEN,   Rank.FOUR,
                                            Rank.KING,  Rank.SEVEN, Rank.SIX);
        GameService gameService = new GameService(noBlackjackDeck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(2);
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


}
