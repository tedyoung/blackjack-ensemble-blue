package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Card;
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
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

    @Test
    public void createGameEndpointCreatesGameAndRedirectsToPlacedBets() throws Exception {
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
    public void createGameUsesCustomDeck() throws Exception {
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
    public void gameViewPopulatesViewModel() throws Exception {
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
    public void placeBetsPageRedirectsToGame() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String nonBlackjackDeck = "2,3,4,5,6,7";
        NewGameForm newGameForm = new NewGameForm(List.of("24", "31"));
        blackjackController.createGame(newGameForm, nonBlackjackDeck);

        Map<String, String> betByPlayerId = Map.of("24", "2", "31", "3");
        BettingForm bettingForm = new BettingForm(betByPlayerId);

        String page = blackjackController.placeBets(bettingForm, true);

        assertThat(gameService.currentBets())
                .containsExactly(new PlayerBet(new PlayerId(24), Bet.of(2)),
                                 new PlayerBet(new PlayerId(31), Bet.of(3)));
        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(2);
        assertThat(page)
                .isEqualTo("redirect:/game");
    }

    @Test
    public void hitCommandDealsAnotherCardToPlayer() throws Exception {
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
    public void hitAndPlayerGoesBustRedirectsToGameDonePage() throws Exception {
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
    public void donePageShowsFinalGameViewWithOutcome() throws Exception {
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
    public void singlePlayerGameStandResultsInGameOver() throws Exception {
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
    public void givenTwoPlayersFirstPlayerGoesBustNextPlayerCanStand() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        String customDeck = new StubDeck(Rank.EIGHT, Rank.NINE, Rank.ACE,
                                         Rank.JACK, Rank.TEN, Rank.FOUR,
                                         Rank.KING, Rank.SEVEN, Rank.SIX).convertToString();
        NewGameForm newGameForm = new NewGameForm(List.of("41", "55"));
        blackjackController.createGame(newGameForm, customDeck);
        blackjackController.placeBets(new BettingForm(List.of(1, 2)), false);
        blackjackController.hitCommand(); // first player is busted

        assertThat(gameService.currentGame().isGameOver())
                .isFalse();

        String page = blackjackController.standCommand(); // second player stands

        assertThat(page)
                .isEqualTo("redirect:/done");

        assertThat(gameService.currentGame().currentPlayerId())
                .isEqualTo(55);
        assertThat(gameService.currentGame().isGameOver())
                .isTrue();
    }

    @Test
    public void singlePlayerDealtBlackjackResultsInGameOver() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService);
        NewGameForm newGameForm = new NewGameForm(List.of("23"));
        blackjackController.createGame(newGameForm, "A,K,Q,7");

        String page = blackjackController.placeBets(new BettingForm(List.of(1)), false);

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
        assertThat(bettingForm.getPlayerIds())
                .containsExactly(23, 52);
        assertThat(bettingForm.getPlayerIdToBets())
                .containsExactlyInAnyOrderEntriesOf(Map.of("23", "0", "52", "0"));
    }

    private static void createGameWithBets(
            BlackjackController blackjackController,
            String customDeck,
            int numberOfPlayers) {
        List<String> playersPlaying = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            playersPlaying.add(String.valueOf(i));
        }

        NewGameForm newGameForm = new NewGameForm(playersPlaying);
        blackjackController.createGame(newGameForm, customDeck);
        List<Integer> bets = IntStream
                .range(1, numberOfPlayers + 1)
                .boxed()
                .toList();
        blackjackController.placeBets(new BettingForm(bets), false);
    }
}
