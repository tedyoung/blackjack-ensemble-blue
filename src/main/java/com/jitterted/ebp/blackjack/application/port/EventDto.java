package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;

public class EventDto {
    /*
        Table draft:

        PK PlayerId-EventId
           JSON String eventContent

        PlayerID | EventId   |   Json
        -----------------------------------------------------------------
        0       | 0          | { type: "PlayerRegistered", name: "Judy"}
        0       | 1          | { type: "MoneyDeposited", amount: 10}
    */

    public static EventDto from(PlayerAccountEvent event) {
        return new EventDto();
    }

}
