package com.jitterted.ebp.blackjack.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public record PlayerWonGame(int payout, PlayerOutcome playerOutcome) implements PlayerAccountEvent {

}
