package com.jitterted.ebp.blackjack.application.port;

import java.util.List;

public interface Shuffler {

    List<Integer> create52ShuffledNumbers();
}
