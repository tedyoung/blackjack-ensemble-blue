package com.jitterted.ebp.blackjack.domain.port;

import com.jitterted.ebp.blackjack.domain.Game;

public interface GameRepository {
    public void saveOutcome(Game game);
}
