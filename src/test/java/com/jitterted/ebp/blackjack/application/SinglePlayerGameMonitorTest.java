package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.DeckFactory;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import org.junit.jupiter.api.Test;

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
        Deck playerCanStandAndDealerCantHit = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        final DeckFactory deckFactory = DeckFactory.createForTest(playerCanStandAndDealerCantHit);
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                new Shoe(deckFactory));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsGoesBustThenGameSendsToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                new Shoe(deckFactory));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerHits();

        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsDoesNotBustThenResultNotSentToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck());
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                new Shoe(deckFactory));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.playerHits();

        verify(gameMonitorSpy, never()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerDealtBlackjackThenSendsGameToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        GameService gameService = new GameService(gameMonitorSpy,
                                                  DUMMY_GAME_REPOSITORY,
                new Shoe(deckFactory));
        gameService.createGame(1);

        gameService.initialDeal();

        verify(gameMonitorSpy).gameCompleted((any(Game.class)));
    }

}
