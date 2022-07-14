package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SinglePlayerGameMonitorTest {

    private static final GameRepository DUMMY_GAME_REPOSITORY = (game) -> { };

    @Test
    public void playerStandsCompletesGameSendsToMonitor() throws Exception {
        // creates the spy based on the interface
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        Deck playerCanStandAndDealerCantHit = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  playerCanStandAndDealerCantHit);
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsGoesBustThenGameSendsToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerHits();

        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsDoesNotBustThenResultNotSentToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck());
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerHits();

        verify(gameMonitorSpy, never()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerDealtBlackjackThenSendsGameToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                                                  SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        gameService.createGame(1);

        gameService.initialDeal();

        verify(gameMonitorSpy).gameCompleted((any(Game.class)));
    }

}
