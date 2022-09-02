package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.DeckFactory;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
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
                                                  new DeckFactory(deck));
        gameService.createGame(2);

        gameService.initialDeal();

        Mockito.verify(gameMonitorSpy, Mockito.never()).gameCompleted(ArgumentMatchers.any(Game.class));
    }
}