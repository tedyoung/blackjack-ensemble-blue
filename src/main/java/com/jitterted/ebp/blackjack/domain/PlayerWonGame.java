package com.jitterted.ebp.blackjack.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public record PlayerWonGame(int payout, PlayerOutcome playerOutcome) implements PlayerAccountEvent {

}
