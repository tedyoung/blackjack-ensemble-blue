package com.jitterted.ebp.blackjack.domain;

public record PlayerWonGame(int payout, PlayerOutcome playerOutcome) implements PlayerAccountEvent {

}
