package com.jitterted.ebp.blackjack.domain;

public class SinglePlayerStubDeckFactory {

    public static Deck createPlayerHitsGoesBustDeck() {
        return new StubDeck(Rank.QUEEN, Rank.EIGHT,
                            Rank.TEN, Rank.FOUR,
                            Rank.THREE);
    }

    public static Deck createPlayerHitsDoesNotBustDeck() {
        return new StubDeck(Rank.QUEEN, Rank.EIGHT,
                            Rank.SEVEN, Rank.FOUR,
                            Rank.THREE);
    }

    public static StubDeck createPlayerDealtBlackjackDeck() {
        return new StubDeck(Rank.KING, Rank.TWO,
                            Rank.ACE, Rank.EIGHT);
    }

    public static StubDeck createPlayerCanStandAndDealerCanNotHitDeck() {
        return new StubDeck(Rank.QUEEN, Rank.KING,
                            Rank.EIGHT, Rank.QUEEN);
    }

    public static StubDeck createPlayerNotDealtBlackjackDeck() {
        return new StubDeck(Rank.QUEEN, Rank.KING,
                            Rank.EIGHT, Rank.QUEEN);
    }
}
