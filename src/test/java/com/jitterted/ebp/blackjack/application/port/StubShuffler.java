package com.jitterted.ebp.blackjack.application.port;

import java.util.ArrayList;
import java.util.List;

public class StubShuffler implements Shuffler {
    @Override
    public List<Integer> create52ShuffledNumbers() {
        List<Integer> cardOrderIndexes = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            cardOrderIndexes.add(i);
        }
        return cardOrderIndexes;
    }
}
