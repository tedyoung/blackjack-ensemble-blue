package com.jitterted.ebp.blackjack.domain;

public sealed interface PlayerAccountEvent
        permits MoneyBet, MoneyDeposited, PlayerLostGame, PlayerRegistered, PlayerWonGame {
}
/*
    Table draft:

    PK PlayerId-EventId
       JSON String eventContent

    0-a | { type: "MoneyDeposited", amount: 10}
*/
