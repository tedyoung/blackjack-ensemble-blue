package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class GameRepositoryTest {

    @Test
    void whenGameOverOutcomeIsSaved() {
        GameRepository repositorySpy = spy(GameRepository.class);
        GameMonitor dummyGameMonitor = spy(GameMonitor.class);
        GameService gameService = new GameService(dummyGameMonitor, repositorySpy, new StubShuffler());
        Deck deck = SinglePlayerStubDeckFactory.createPlayerCanStandAndDealerCanNotHitDeck();
        Shoe shoe = new Shoe(List.of(deck));
        gameService.createGame(1, shoe);
        gameService.initialDeal();

        gameService.playerStands();

        // verify that the roundCompleted method was called with any instance of a Game class
        verify(repositorySpy).saveOutcome(any(Game.class));
    }
}