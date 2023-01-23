package com.jitterted.ebp.blackjack.adapter.out.shuffler;

import com.jitterted.ebp.blackjack.application.port.Shuffler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomShuffler implements Shuffler {
    @Override
    public List<Integer> create52ShuffledNumbers() {
        List<Integer> cardOrderIndexes = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            cardOrderIndexes.add(i);
        }
        Collections.shuffle(cardOrderIndexes);
        return cardOrderIndexes;
    }
}
