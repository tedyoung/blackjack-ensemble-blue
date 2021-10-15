package com.jitterted.ebp.blackjack.domain;

public class MultiPlayerStubDeckFactory {

    public static StubDeck twoPlayersNotDealtBlackjack() {
        return new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                            Rank.EIGHT, Rank.QUEEN, Rank.NINE);
    }

    public static StubDeck threePlayersNotDealtBlackjack() {
        return new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN, Rank.SEVEN,
                            Rank.EIGHT, Rank.QUEEN, Rank.NINE, Rank.TEN);
    }
}
