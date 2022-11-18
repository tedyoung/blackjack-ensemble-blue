package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;

import java.util.Arrays;

public class CustomDeckParser {
    static Deck createCustomDeck(String customDeck) {
        Rank[] ranks = Arrays.stream(customDeck.split(","))
                             .map(CustomDeckParser::rankFromString)
                             .toList().toArray(new Rank[0]);
        return new StubDeck(ranks);
    }

    static Rank rankFromString(String displayRank) {
        return Arrays.stream(Rank.values())
                     .filter(rank -> rank.display().equalsIgnoreCase(displayRank))
                     .findFirst()
                     .orElseThrow();
    }
}
