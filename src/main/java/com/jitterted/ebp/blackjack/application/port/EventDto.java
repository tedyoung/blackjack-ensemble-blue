package com.jitterted.ebp.blackjack.application.port;

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

}
