package com.jitterted.ebp.blackjack.domain.port;

import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class GameRepositoryTest {
    @Test
    void whenGameOverOutcomeIsSaved() {
        GameRepository repositorySpy = spy(GameRepository.class);
        GameMonitor gameMonitor = spy(GameMonitor.class);
        Deck playerCanStandAndDealerCantHit = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        Game game = new Game(playerCanStandAndDealerCantHit, gameMonitor, repositorySpy, 1);
        game.initialDeal();

        game.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(repositorySpy).saveOutcome(any(Game.class));
    }
}