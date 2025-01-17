package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

    @Test
    void createGameEndpointCreatesGameAndRedirectsToPlacedBets() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());

        String redirect = blackjackController.createGame(
                createPlayerSelectionForm(List.of(7L)), "");

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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());

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
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(17);
        playerAccountRepository.save(PlayerAccount.register("Mike"));
        BlackjackController blackjackController = new BlackjackController(gameService, playerAccountRepository);
        blackjackController.createGame(createPlayerSelectionForm(List.of(17L)), deck.convertToString());

        Model model = new ConcurrentModel();
        blackjackController.gameInProgressView(model);

        assertThat(model.getAttribute("gameInProgressView"))
                .isNotNull();
    }

    @Test
    void placeBetsPageRedirectsToGame() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
        String nonBlackjackDeck = "2,3,4,5,6,7";
        blackjackController.createGame(createPlayerSelectionForm(List.of(24L, 31L)),
                                       nonBlackjackDeck);

        Map<String, String> betByPlayerId = Map.of("24", "2", "31", "3");
        BettingForm bettingForm = new BettingForm(betByPlayerId, Collections.emptyMap());

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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
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
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        playerAccountRepository.save(PlayerAccount.register("James"));
        BlackjackController blackjackController = new BlackjackController(gameService, playerAccountRepository);
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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
        String customDeck = new StubDeck(Rank.EIGHT, Rank.NINE, Rank.ACE,
                                         Rank.JACK, Rank.TEN, Rank.FOUR,
                                         Rank.KING, Rank.SEVEN, Rank.SIX).convertToString();
        blackjackController.createGame(createPlayerSelectionForm(List.of(41L, 55L)), customDeck);
        BettingForm bettingForm = new BettingForm(Map.of("41", "1", "55", "2"), Collections.emptyMap());
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
        BlackjackController blackjackController = new BlackjackController(gameService, new PlayerAccountRepository());
        blackjackController.createGame(createPlayerSelectionForm(List.of(23L)), "A,K,Q,7");

        BettingForm bettingForm = new BettingForm(Map.of("23", "1"), Collections.emptyMap());
        String page = blackjackController.placeBets(bettingForm);

        assertThat(page)
                .isEqualTo("redirect:/done");
    }

    @Test
    void bettingFormHasPlayerIdsAndNamesFromCreatedGame() {
        GameService gameService = GameService.createForTest(new StubShuffler());
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(53);
        PlayerAccount fred = createPlayerAccount(playerAccountRepository, "Fred", 15);
        PlayerAccount george = createPlayerAccount(playerAccountRepository, "George", 35);
        BlackjackController blackjackController = new BlackjackController(
                gameService, playerAccountRepository);
        gameService.createGame(List.of(fred.getPlayerId(), george.getPlayerId()));

        Model model = new ConcurrentModel();
        blackjackController.showBettingForm(model);

        BettingForm bettingForm = (BettingForm) model.getAttribute("bettingForm");
        assertThat(bettingForm.getPlayerIdToBets())
                .containsExactlyInAnyOrderEntriesOf(Map.of("53", "0", "54", "0"));
        assertThat(bettingForm.getPlayerIdToNames())
                .containsExactlyInAnyOrderEntriesOf(Map.of("53", "Fred $15", "54", "George $35"));
    }

    private PlayerAccount createPlayerAccount(PlayerAccountRepository playerAccountRepository, String playerName, int initialBalance) {
        PlayerAccount fred = PlayerAccount.register(playerName);
        fred.deposit(initialBalance);
        return playerAccountRepository.save(fred);
    }

    private static void createGameWithBets(
            BlackjackController blackjackController,
            String customDeck,
            int numberOfPlayers) {
        List<Long> playersPlaying = new ArrayList<>();
        Map<String, String> newBets = new HashMap<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            playersPlaying.add((long) i);
            newBets.put(String.valueOf(i), String.valueOf(i + 1));
        }

        blackjackController.createGame(createPlayerSelectionForm(playersPlaying), customDeck);

        blackjackController.placeBets(new BettingForm(newBets, Collections.emptyMap()));
    }

    private static PlayerSelectionForm createPlayerSelectionForm(List<Long> playersPlayingIds) {
        PlayerSelectionForm playerSelectionForm = new PlayerSelectionForm(Collections.emptyList());
        playerSelectionForm.setPlayersPlaying(playersPlayingIds);
        return playerSelectionForm;
    }

}
