package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;

class MultiPlayerGameMonitorTest {

    private static final GameRepository DUMMY_GAME_REPOSITORY = (game) -> {
    };

    @Test
    void twoPlayersWhenFirstPlayerIsDoneThenMonitorIsNotCalledBecauseGameIsNotOver() throws Exception {
        GameMonitor gameMonitorSpy = Mockito.spy(GameMonitor.class);
        GameService gameService = new GameService(
                gameMonitorSpy, DUMMY_GAME_REPOSITORY, new StubShuffler());
        StubDeck deck = new StubDeck(Rank.KING, Rank.TWO, Rank.JACK,
                                     Rank.ACE, Rank.EIGHT, Rank.SEVEN);
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(List.of(PlayerId.of(12), PlayerId.of(21)), shoe);
        gameService.placePlayerBets(List.of(
                new PlayerBet(PlayerId.of(12), Bet.of(11)),
                new PlayerBet(PlayerId.of(21), Bet.of(22))));

        gameService.initialDeal();

        Mockito.verify(gameMonitorSpy, Mockito.never()).gameCompleted(ArgumentMatchers.any(Game.class));
    }
}