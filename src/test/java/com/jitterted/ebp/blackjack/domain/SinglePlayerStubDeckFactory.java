package com.jitterted.ebp.blackjack.domain;

public class SinglePlayerStubDeckFactory {

    public static Deck createPlayerHitsGoesBustDeckAndDealerCanNotHit() {
        return new StubDeck(Rank.QUEEN, Rank.EIGHT,
                            Rank.TEN,   Rank.TEN,
                            Rank.THREE);
    }

    public static Deck createPlayerHitsGoesBustDeckAndDealerCouldHit() {
        return new StubDeck(Rank.QUEEN, Rank.EIGHT,
                            Rank.TEN,   Rank.FIVE,
                            Rank.THREE, Rank.FOUR);
    }

    public static Deck createPlayerHitsDoesNotBustDeck() {
        return new StubDeck(Rank.QUEEN, Rank.EIGHT,
                            Rank.SEVEN, Rank.FOUR,
                            Rank.THREE);
    }

    public static StubDeck createPlayerDealtBlackjackDeckAndDealerCanNotHit() {
        return new StubDeck(Rank.KING, Rank.TEN,
                            Rank.ACE, Rank.EIGHT);
    }

    public static StubDeck createPlayerDealtBlackjackDeckAndDealerCouldHit() {
        return new StubDeck(Rank.KING, Rank.SEVEN,
                            Rank.ACE,  Rank.EIGHT,
                                       Rank.THREE);
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
