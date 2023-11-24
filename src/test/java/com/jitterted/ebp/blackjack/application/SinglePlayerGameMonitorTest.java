package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.StubShuffler;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
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

    @Test
    public void playerStandsCompletesGameSendsToMonitor() throws Exception {
        Fixture fixture = createOnePlayerGamePlaceBetsInitialDeal(
                SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck());

        fixture.gameService().playerStands();

        verify(fixture.gameMonitorSpy()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsGoesBustThenGameSendsToMonitor() throws Exception {
        Fixture fixture = createOnePlayerGamePlaceBetsInitialDeal(
                SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());

        fixture.gameService().playerHits();

        verify(fixture.gameMonitorSpy()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerHitsDoesNotBustThenResultNotSentToMonitor() throws Exception {
        Fixture fixture = createOnePlayerGamePlaceBetsInitialDeal(
                SinglePlayerStubDeckFactory.createPlayerHitsDoesNotBustDeck());

        fixture.gameService().playerHits();

        verify(fixture.gameMonitorSpy(), never()).gameCompleted(any(Game.class));
    }

    @Test
    public void playerDealtBlackjackThenSendsGameToMonitor() throws Exception {
        Fixture fixture = createOnePlayerGamePlaceBets(
                SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());

        fixture.gameService().initialDeal();

        verify(fixture.gameMonitorSpy()).gameCompleted((any(Game.class)));
    }

    private static Fixture createOnePlayerGamePlaceBetsInitialDeal(StubDeck deck) {
        Fixture fixture = createOnePlayerGamePlaceBets(deck);
        fixture.gameService().initialDeal();
        return fixture;
    }

    private static Fixture createOnePlayerGamePlaceBets(Deck deck) {
        GameRepository dummyGameRepository = (game) -> {
        };

        GameMonitor gameMonitorSpy = spy(GameMonitor.class);
        GameService gameService = new GameService(gameMonitorSpy, dummyGameRepository, new StubShuffler());
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(List.of(PlayerId.of(7)), shoe);
        gameService.placePlayerBets(List.of(new PlayerBet(PlayerId.of(7), Bet.of(11))));
        return new Fixture(gameMonitorSpy, gameService);
    }

    private record Fixture(GameMonitor gameMonitorSpy, GameService gameService) {
    }

}
