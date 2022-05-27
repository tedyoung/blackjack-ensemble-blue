package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.Game;

public interface GameMonitor {
    void gameCompleted(Game game);
}
