package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameBuilder;
import com.jitterted.ebp.blackjack.domain.MultiPlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeViewTest {

    @Test
    void twoPlayerGameAndGameIsOverThenHasTwoPlayerOutcomes() throws Exception {
        StubDeck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);
        game.playerStands();
        game.playerStands();
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        playerAccountRepository.save(PlayerAccount.register("Mike"));
        playerAccountRepository.save(PlayerAccount.register("Anna"));

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
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);
        game.playerStands();
        game.playerStands();

        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game, new PlayerAccountRepository());

        assertThat(gameOutcomeView.getDealerCards())
                .containsExactly("10♥", "9♥");
    }

}