package com.jitterted.ebp.blackjack.domain;

public class MultiPlayerStubDeckFactory {

    public static StubDeck twoPlayersNotDealtBlackjack() {
        return new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                            Rank.EIGHT, Rank.QUEEN, Rank.NINE);
    }
}
