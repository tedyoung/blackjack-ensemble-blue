package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class BettingFormTest {

    @Nested
    class ShowingForm {

        @Test
        void bettingFormContainsPlayerIdsAndNames() {
            PlayerAccountFinder playerAccountFinder = new PlayerAccountFinder() {
                final Map<PlayerId, PlayerAccount> playerAccounts = Map.of(
                        PlayerId.of(15), PlayerAccount.register("Frida"),
                        PlayerId.of(73), PlayerAccount.register("Alice")
                );

                @Override
                public Optional<PlayerAccount> find(PlayerId playerId) {
                    return Optional.of(playerAccounts.get(playerId));
                }
            };
            BettingForm bettingForm = BettingForm.zeroBetsFor(
                    playerAccountFinder,
                    List.of(
                            PlayerId.of(15),
                            PlayerId.of(73)
                    ));

            assertThat(bettingForm.getPlayerIdToNames())
                    .containsAllEntriesOf(Map.of("15", "Frida",
                                                 "73", "Alice"));
        }
    }

    @Nested
    class SubmittingBets {

        @Test
        void returnsPlayerBetsForSinglePlayer() {
            Map<String, String> playerIdToBets = Map.of("25", "55");
            BettingForm bettingForm = new BettingForm(playerIdToBets, Collections.emptyMap());

            List<PlayerBet> playerBets = bettingForm.getPlayerBets();

            assertThat(playerBets).containsExactly(
                    new PlayerBet(PlayerId.of(25), Bet.of(55)));
        }

        @Test
        void returnsPlayerBetsForMultiplePlayers() {
            Map<String, String> playerBetsMap = Map.of(
                    "21", "66",
                    "13", "75"
            );
            BettingForm bettingForm = new BettingForm(playerBetsMap, Collections.emptyMap());

            List<PlayerBet> playerBets = bettingForm.getPlayerBets();

            assertThat(playerBets).containsExactlyInAnyOrder(
                    new PlayerBet(PlayerId.of(21), Bet.of(66)),
                    new PlayerBet(PlayerId.of(13), Bet.of(75)));
        }

    }
}