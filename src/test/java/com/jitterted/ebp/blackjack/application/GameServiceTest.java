package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class GameServiceTest {
    @Test
    void startGameWithOnePlayerCountIsOne() {
        GameService gameService = GameService.createForTest(new StubShuffler());

        gameService.createGame(List.of(PlayerId.of(1)));

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    void startGameWithTwoPlayersCountIsTwo() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());

        gameService.createGame(List.of(PlayerId.of(55), PlayerId.of(87)));

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }

    @Test
    void createGameCreatesShuffledDeckUsingShuffler() throws Exception {
        GameService gameService = GameService.createForTest(new StubShuffler());
        gameService.createGame(List.of(PlayerId.of(87)));
        gameService.placePlayerBets(List.of(new PlayerBet(PlayerId.of(87), Bet.of(11))));

        gameService.initialDeal();

        assertThat(gameService.currentGame().currentPlayerCards())
                .containsExactly(new Card(Suit.HEARTS, Rank.ACE),
                                 new Card(Suit.HEARTS, Rank.THREE));
    }

    @Test
    void createGameCreatesDeckInReverseOrderUsingShuffler() {
        GameService gameService = GameService.createForTest(() -> {
            List<Integer> cardOrderIndexes = new ArrayList<>();
            for (int i = 51; i >= 0; i--) {
                cardOrderIndexes.add(i);
            }
            return cardOrderIndexes;
        });
        gameService.createGame(List.of(PlayerId.of(5)));
        gameService.placePlayerBets(List.of(new PlayerBet(PlayerId.of(5), Bet.of(11))));

        gameService.initialDeal();

        assertThat(gameService.currentGame().currentPlayerCards())
                .containsExactly(new Card(Suit.SPADES, Rank.KING),
                                 new Card(Suit.SPADES, Rank.JACK));
    }

    @Test
    void whenGameOverOutcomeIsSaved() {
        GameRepository repositorySpy = spy(GameRepository.class);
        GameMonitor dummyGameMonitor = spy(GameMonitor.class);
        GameService gameService = new GameService(dummyGameMonitor, repositorySpy, new StubShuffler());
        Deck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(List.of(PlayerId.of(9)), shoe);
        gameService.placePlayerBets(List.of(new PlayerBet(PlayerId.of(9), Bet.of(11))));
        gameService.initialDeal();

        gameService.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(repositorySpy).saveOutcome(any(Game.class));
    }

    @Test
    void placeBetsReducesPlayerAccountBalance () {
        GameService gameService = GameService.createForTest(new StubShuffler());
        // TODO: need a player account object in the repository that has id 7 and some initial balance of 30
        gameService.createGame(List.of(PlayerId.of(7)));

        gameService.placePlayerBets(List.of(new PlayerBet(PlayerId.of(7), Bet.of(13))));

        // assertThat(repository.findByPlayerId(7)).balance should be 30 - 13
        fail("Start here");
    }
}