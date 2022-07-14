package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.Game;

public interface GameRepository {
    public void saveOutcome(Game game);
}
