package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameOutcomeDtoTest {
    @Test
    public void playerStandsGameIsOverAsString() throws Exception {
        Game game = new Game(StubDeck(List.of(new Card(Suit.HEARTS, Rank.QUEEN
                        ))));

        String gameAsString = game.gameAsString();
        assertThat(gameAsString).isEqualTo("Q♥/8♥,3♥/5♥/J♥");
    }
}
