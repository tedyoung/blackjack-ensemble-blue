package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.Game;

@FunctionalInterface
public interface GameCommand {

    void execute(Game game);

}
