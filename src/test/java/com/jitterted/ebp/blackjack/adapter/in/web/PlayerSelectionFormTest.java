package com.jitterted.ebp.blackjack.adapter.in.web;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSelectionFormTest {

    @Test
    void ofTakesAListOfPlayerAccountViewsAndThenGetPlayersReturnsThatList() {
        PlayerSelectionForm.of(List.of(new PlayerAccountView()));
    }
}