package com.jitterted.ebp.blackjack.domain;

public record PlayerDoneEvent(PlayerId playerId, PlayerReasonDone reasonDone) {
}