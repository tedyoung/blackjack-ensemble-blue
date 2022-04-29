package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.PlayerOutcome;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class OutcomeDtoTest {

    @ParameterizedTest(name = "{index}: {0} -> {1}")
    @MethodSource("expectedValue")
    public void playerOutcomeEnumTransformedByDtoToString(PlayerOutcome gameOutcome, String expectedResult) throws Exception {
        OutcomeDto outcomeDto = new OutcomeDto(gameOutcome);

        assertThat(outcomeDto.asString()).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> expectedValue() {
        return Stream.of(
                Arguments.of(PlayerOutcome.PLAYER_BUSTED, "Player Busted"),
                Arguments.of(PlayerOutcome.DEALER_BUSTED, "Dealer Busted"),
                Arguments.of(PlayerOutcome.PLAYER_LOSES, "Player Loses"),
                Arguments.of(PlayerOutcome.BLACKJACK, "Player Wins Blackjack"),
                Arguments.of(PlayerOutcome.PLAYER_BEATS_DEALER, "Player Beats Dealer"),
                Arguments.of(PlayerOutcome.PLAYER_PUSHES_DEALER, "Player Pushes Dealer")
        );
    }
}
