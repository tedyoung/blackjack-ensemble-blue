package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.PlayerInGame;
import com.jitterted.ebp.blackjack.domain.PlayerOutcome;
import com.jitterted.ebp.blackjack.domain.PlayerResult;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerOutcomeViewTest {

    @Test
    void playerHasBlackjackThenDisplaysIdCardsAndOutcome() throws Exception {
        Deck deck = new StubDeck(Rank.KING, Rank.ACE);
        int playerId = 7;
        Fixture fixture = createPlayerInRepositoryAndInitialDeal(PlayerId.of(playerId), deck);
        PlayerResult playerResult = new PlayerResult(fixture.player(),
                                                     PlayerOutcome.BLACKJACK,
                                                     Bet.of(20));

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView.from(playerResult,
                                                                     fixture.playerAccountRepository());

        assertThat(playerOutcomeView.getPlayerId())
                .isEqualTo(7);
        assertThat(playerOutcomeView.getPlayerCards())
                .hasSize(2)
                .containsOnly("K♥", "A♥");
        assertThat(playerOutcomeView.getPlayerOutcome())
                .isEqualTo("BLACKJACK");
        assertThat(playerOutcomeView.getBetOutcome())
                .isEqualTo("You bet 20 and got back 50");
    }

    @Test
    void playerHasLostThenDisplaysBetOutcome() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.TEN, Rank.TEN)
                                   .buildWithDealerDealtBlackjack();
        Fixture fixture = createPlayerInRepositoryAndInitialDeal(PlayerId.of(3), deck);
        PlayerResult playerResult = new PlayerResult(fixture.player,
                                                     PlayerOutcome.PLAYER_LOSES,
                                                     Bet.of(25));

        PlayerOutcomeView playerOutcomeView = PlayerOutcomeView
                .from(playerResult,
                      fixture.playerAccountRepository);

        assertThat(playerOutcomeView.getBetOutcome())
                .isEqualTo("You bet 25 and got back 0");
    }

    @Test
    void playerNotFoundThrowsException() {
        PlayerAccountFinder repository = new PlayerAccountRepository();
        PlayerInGame player = new PlayerInGame(PlayerId.of(37));
        PlayerResult playerResult = new PlayerResult(player,
                                                     PlayerOutcome.PLAYER_LOSES,
                                                     Bet.of(25));
        assertThatThrownBy(() -> PlayerOutcomeView.from(playerResult, repository))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("PlayerAccount not found for PlayerId[id=37]");
    }

    private Fixture createPlayerInRepositoryAndInitialDeal(PlayerId playerId, Deck deck) {
        PlayerInGame player = new PlayerInGame(playerId);
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(player.playerId().id());
        playerAccountRepository.save(PlayerAccount.register("Ted"));
        Shoe shoe = new Shoe(List.of(deck));
        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);
        return new Fixture(playerAccountRepository, player);
    }

    private record Fixture(PlayerAccountRepository playerAccountRepository, PlayerInGame player) {
    }
}