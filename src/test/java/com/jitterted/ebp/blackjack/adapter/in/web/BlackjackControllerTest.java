package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

    @Test
    void homepageReturnsIndexTemplate() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
    }

    @Test
    void createGameEndpointCreatesGameAndRedirectsToPlacedBets() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        NewGameForm newGameForm = new NewGameForm(List.of("7"));

        String redirect = blackjackController.createGame(newGameForm, "");

        assertThat(redirect)
                .isEqualTo("redirect:/place-bets");
        assertThat(gameService.currentGame())
                .isNotNull();
        assertThat(gameService.currentGame().currentPlayerCards())
                .isEmpty();
        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    void createGameUsesCustomDeck() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);

        createGameWithBets(blackjackController, "8,Q,K,2", 1);

        assertThat(gameService.currentGame().currentPlayerCards())
                .extracting(Card::rank)
                .containsExactly(Rank.EIGHT, Rank.KING);
        assertThat(gameService.currentGame().dealerHand().cards())
                .extracting(Card::rank)
                .containsExactly(Rank.QUEEN, Rank.TWO);
    }

    @Test
    void gameViewPopulatesViewModel() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerHitsAndGoesBust()
                                       .buildWithDealerDoesNotDrawCards();
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        NewGameForm newGameForm = new NewGameForm(List.of("17"));
        blackjackController.createGame(newGameForm, deck.convertToString());

        Model model = new ConcurrentModel();
        blackjackController.gameInProgressView(model);

        assertThat(model.getAttribute("gameInProgressView"))
                .isNotNull();
    }

    @Test
    void placeBetsPageRedirectsToGame() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String nonBlackjackDeck = "2,3,4,5,6,7";
        NewGameForm newGameForm = new NewGameForm(List.of("24", "31"));
        blackjackController.createGame(newGameForm, nonBlackjackDeck);

        Map<String, String> betByPlayerId = Map.of("24", "2", "31", "3");
        BettingForm bettingForm = new BettingForm(betByPlayerId);

        String page = blackjackController.placeBets(bettingForm);

        assertThat(gameService.currentBets())
                .containsExactly(new PlayerBet(PlayerId.of(24), Bet.of(2)),
                                 new PlayerBet(PlayerId.of(31), Bet.of(3)));
        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(2);
        assertThat(page)
                .isEqualTo("redirect:/game");
    }

    @Test
    void hitCommandDealsAnotherCardToPlayer() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck()
                                                       .convertToString();
        createGameWithBets(blackjackController, customDeck, 1);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/game");

        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(3);
    }


    @Test
    void hitAndPlayerGoesBustRedirectsToGameDonePage() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit()
                                                       .convertToString();
        createGameWithBets(blackjackController, customDeck, 1);

        String redirect = blackjackController.hitCommand();

        assertThat(redirect)
                .isEqualTo("redirect:/done");
    }

    @Test
    void donePageShowsFinalGameViewWithOutcome() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck()
                                                       .convertToString();
        createGameWithBets(blackjackController, customDeck, 1);
        blackjackController.standCommand();

        Model model = new ConcurrentModel();
        blackjackController.viewDone(model);

        assertThat(model.getAttribute("gameOutcomeView"))
                .isInstanceOf(GameOutcomeView.class);
    }

    @Test
    void singlePlayerGameStandResultsInGameOver() throws Exception {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        createGameWithBets(blackjackController, deck.convertToString(), 1);

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
        createGameWithBets(blackjackController, customDeck, 2);

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");
    }

    @Test
    void givenTwoPlayersFirstPlayerGoesBustNextPlayerCanStand() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = new StubDeck(Rank.EIGHT, Rank.NINE, Rank.ACE,
                                         Rank.JACK, Rank.TEN, Rank.FOUR,
                                         Rank.KING, Rank.SEVEN, Rank.SIX).convertToString();
        NewGameForm newGameForm = new NewGameForm(List.of("41", "55"));
        blackjackController.createGame(newGameForm, customDeck);
        BettingForm bettingForm = new BettingForm(Map.of("41", "1", "55", "2"));
        blackjackController.placeBets(bettingForm);
        blackjackController.hitCommand(); // first player is busted

        assertThat(gameService.currentGame().isGameOver())
                .isFalse();

        String page = blackjackController.standCommand(); // second player stands

        assertThat(page)
                .isEqualTo("redirect:/done");

        Game game = gameService.currentGame();
        assertThat(game.currentPlayerId())
                .isEqualTo(PlayerId.of(55));
        assertThat(gameService.currentGame().isGameOver())
                .isTrue();
    }

    @Test
    void singlePlayerDealtBlackjackResultsInGameOver() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        NewGameForm newGameForm = new NewGameForm(List.of("23"));
        blackjackController.createGame(newGameForm, "A,K,Q,7");

        BettingForm bettingForm = new BettingForm(Map.of("23", "1"));
        String page = blackjackController.placeBets(bettingForm);

        assertThat(page)
                .isEqualTo("redirect:/done");
    }

    @Test
    void bettingFormHasPlayerIdsFromCreatedGame() {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        gameService.createGame(List.of(PlayerId.of(23), PlayerId.of(52)));

        Model model = new ConcurrentModel();
        blackjackController.showBettingForm(model);

        BettingForm bettingForm = (BettingForm) model.getAttribute("bettingForm");
        assertThat(bettingForm.getPlayerIdToBets())
                .containsExactlyInAnyOrderEntriesOf(Map.of("23", "0", "52", "0"));
    }

    private static void createGameWithBets(
            BlackjackController blackjackController,
            String customDeck,
            int numberOfPlayers) {
        List<String> playersPlaying = new ArrayList<>();
        Map<String, String> newBets = new HashMap<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            playersPlaying.add(String.valueOf(i));
            newBets.put(String.valueOf(i), String.valueOf(i + 1));
        }

        NewGameForm newGameForm = new NewGameForm(playersPlaying);
        blackjackController.createGame(newGameForm, customDeck);

        blackjackController.placeBets(new BettingForm(newBets));
    }
}
