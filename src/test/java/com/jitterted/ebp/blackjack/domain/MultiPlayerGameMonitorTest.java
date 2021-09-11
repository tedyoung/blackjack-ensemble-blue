package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class MultiPlayerGameMonitorTest {

    @Test
    public void twoPlayersWhenFirstPlayerIsDoneThenMonitorIsNotCalled() throws Exception {
        GameMonitor gameMonitorSpy = Mockito.spy(GameMonitor.class);
        StubDeck deck = new StubDeck(Rank.KING, Rank.TWO, Rank.JACK,
                                     Rank.ACE, Rank.EIGHT, Rank.SEVEN);
        Game game = new Game(deck, gameMonitorSpy, 2);

        game.initialDeal();

        Mockito.verify(gameMonitorSpy, Mockito.never()).gameCompleted(ArgumentMatchers.any(Game.class));
    }
}