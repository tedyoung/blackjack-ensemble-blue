package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

public class PlayerAccountRepository {

    public PlayerAccount load(PlayerId playerId) {
        var account = PlayerAccount.register("");
        account.setId(playerId);
        return account;
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        return null;
    }
}
