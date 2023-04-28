package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
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
        GameService gameService = new GameService(gameMonitorSpy, DUMMY_GAME_REPOSITORY, new StubShuffler());
        Deck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(1, shoe);
        gameService.initialDeal();

        gameService.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(gameMonitorSpy).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsGoesBustThenGameSendsToMonitor() throws Exception {
        Fixture fixture = createOnePlayerGamePlaceBetsInitialDeal(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());

        fixture.gameService().playerHits();

        verify(fixture.gameMonitorSpy()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsDoesNotBustThenResultNotSentToMonitor() throws Exception {
        Fixture fixture = createOnePlayerGamePlaceBetsInitialDeal(SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck());

        fixture.gameService().playerHits();

        verify(fixture.gameMonitorSpy(), never()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerDealtBlackjackThenSendsGameToMonitor() throws Exception {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        GameService gameService = new GameService(gameMonitorSpy, DUMMY_GAME_REPOSITORY, new StubShuffler());
        Deck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(1, shoe);

        gameService.initialDeal();

        verify(gameMonitorSpy).gameCompleted((any(Game.class)));
    }

    private Fixture createOnePlayerGamePlaceBetsInitialDeal(StubDeck deck) {
        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        GameService gameService = new GameService(gameMonitorSpy, DUMMY_GAME_REPOSITORY, new StubShuffler());
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(1, shoe);
        gameService.placeBets(List.of(Bet.of(11)));
        gameService.initialDeal();
        return new Fixture(gameMonitorSpy, gameService);
    }

    private record Fixture(GameMonitor gameMonitorSpy, GameService gameService) {
    }

}
