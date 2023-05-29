package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PlayerTest {

    @Test
    void playerCannotHitWhenDone() {
        Fixture fixture = createPlayerThatIsDone();

        assertThatThrownBy(() -> fixture.player().hit(fixture.shoe()))
                .isExactlyInstanceOf(PlayerAlreadyDone.class);
    }

    @Test
    void playerCannotStandWhenDone() {
        Fixture fixture = createPlayerThatIsDone();

        assertThatThrownBy(() -> fixture.player().stand())
                .isExactlyInstanceOf(PlayerAlreadyDone.class);
    }

    private static Fixture createPlayerThatIsDone() {
        Player player = new Player();
        StubDeck deck = new StubDeck(Rank.JACK, Rank.NINE, Rank.FOUR);
        Shoe shoe = new Shoe(List.of(deck));
        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);
        player.hit(shoe);
        return new Fixture(player, shoe);
    }

    private record Fixture(Player player, Shoe shoe) {
    }
}
