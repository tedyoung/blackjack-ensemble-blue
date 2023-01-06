package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SinglePlayerGameMonitorTest {

    private static final GameRepository DUMMY_GAME_REPOSITORY = (game) -> {
    };

    @Test
    public void playerStandsCompletesGameSendsToMonitor() throws Exception {
        // creates the spy based on the interface
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        Deck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = GameService.createForTest(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  new Shoe(List.of(deck)));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsGoesBustThenGameSendsToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        GameService gameService = GameService.createForTest(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  new Shoe(List.of(deck)));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerHits();

        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsDoesNotBustThenResultNotSentToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck();
        GameService gameService = GameService.createForTest(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  new Shoe(List.of(deck)));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerHits();

        verify(gameMonitorSpy, never()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerDealtBlackjackThenSendsGameToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        Deck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        GameService gameService = GameService.createForTest(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  new Shoe(List.of(deck)));
        gameService.createGame(1);

        gameService.initialDeal();

        verify(gameMonitorSpy).gameCompleted((any(Game.class)));
    }

}
