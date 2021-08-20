package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.GameService;
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

        String redirect = blackjackController.startGame();

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame())
                .isNotNull();
        assertThat(gameService.currentGame().playerCards())
                .hasSize(2);
        assertThat(gameService.currentGame().playerCount())
        		.isEqualTo(2);
    }

    @Test
    public void gameViewPopulatesViewModel() throws Exception {
        GameService gameService = new GameService();
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame();

        Model model = new ConcurrentModel();
        blackjackController.gameView(model);

        assertThat(model.getAttribute("gameView"))
                .isNotNull();
    }

    @Test
    public void hitCommandDealsAnotherCardToPlayer() throws Exception {
        GameService gameService = new GameService(StubDeck.createPlayerHitsDoesNotBustDeck());
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame();

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame().playerCards())
                .hasSize(3);
    }

    @Test
    public void hitAndPlayerGoesBustRedirectsToGameDonePage() throws Exception {
        Deck deck = StubDeck.createPlayerHitsGoesBustDeck();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame();

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void donePageShowsFinalGameViewWithOutcome() throws Exception {
        Deck deck = StubDeck.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame();
        blackjackController.standCommand();

        Model model = new ConcurrentModel();
        blackjackController.viewDone(model);

        assertThat(model.getAttribute("gameView"))
                .isNotNull();
        String outcome = (String) model.getAttribute("outcome");
        assertThat(outcome)
                .isNotBlank();
    }

    @Test
    public void standResultsInGamePlayerIsDone() throws Exception {
        Deck deck = StubDeck.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame();

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/done");

        assertThat(gameService.currentGame().isPlayerDone())
                .isTrue();
    }

}
