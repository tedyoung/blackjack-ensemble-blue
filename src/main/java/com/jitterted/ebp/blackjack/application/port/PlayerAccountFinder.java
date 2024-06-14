package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.Optional;

public interface PlayerAccountFinder {
    Optional<PlayerAccount> find(PlayerId playerId);
}
