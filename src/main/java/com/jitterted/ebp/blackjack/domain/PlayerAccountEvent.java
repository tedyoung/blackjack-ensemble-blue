package com.jitterted.ebp.blackjack.domain;

public sealed interface PlayerAccountEvent
        permits MoneyBet, MoneyDeposited, PlayerRegistered, PlayerWonGame {
}
