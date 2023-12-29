package com.jitterted.ebp.blackjack.domain;

public record PlayerLostGame(PlayerOutcome playerOutcome) implements PlayerAccountEvent {
}
