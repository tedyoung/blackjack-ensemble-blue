package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameBuilder;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeViewTest {

    @Test
    void twoPlayerGameAndGameIsOverThenHasTwoPlayerOutcomes() throws Exception {
        StubDeck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        Game game = GameBuilder.createTwoPlayerGameWithPlayersInRepository(playerAccountRepository, deck, "Mike", "Anna");
        game.playerStands();
        game.playerStands();

        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game, playerAccountRepository);

        assertThat(gameOutcomeView.getPlayerOutcomes().get(0).getPlayerName())
                .isEqualTo("Mike");
        assertThat(gameOutcomeView.getPlayerOutcomes().get(1).getPlayerName())
                .isEqualTo("Anna");
    }

    @Test
    void gameIsOverThenHasDealerCards() throws Exception {
        StubDeck deck = new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                                     Rank.EIGHT, Rank.QUEEN, Rank.NINE);
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        Game game = GameBuilder.createTwoPlayerGameWithPlayersInRepository(playerAccountRepository, deck);
        game.playerStands();
        game.playerStands();

        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game, playerAccountRepository);

        assertThat(gameOutcomeView.getDealerCards())
                .containsExactly("10♥", "9♥");
    }

}