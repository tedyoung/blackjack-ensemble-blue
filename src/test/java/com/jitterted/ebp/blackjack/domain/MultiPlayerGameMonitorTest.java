package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class MultiPlayerGameMonitorTest {

    private static final GameRepository DUMMY_GAME_REPOSITORY = (game) -> { };

    @Test
    public void twoPlayersWhenFirstPlayerIsDoneThenMonitorIsNotCalledBecauseGameIsNotOver() throws Exception {
        GameMonitor gameMonitorSpy = Mockito.spy(GameMonitor.class);
        StubDeck deck = new StubDeck(Rank.KING, Rank.TWO, Rank.JACK,
                                     Rank.ACE, Rank.EIGHT, Rank.SEVEN);
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  deck);
        gameService.createGame(2);

        gameService.initialDeal();

        Mockito.verify(gameMonitorSpy, Mockito.never()).gameCompleted(ArgumentMatchers.any(Game.class));
    }
}