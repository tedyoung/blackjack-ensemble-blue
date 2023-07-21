package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class OrderedDeckTest {

    @Test
    void givenListOfOneIndexReturnsThreeOfHearts() {
        List<Integer> cardOrderIndexes = List.of(2);

        OrderedDeck deck = new OrderedDeck(cardOrderIndexes);

        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.THREE));
    }

    @Test
    void givenListOfTwoIndexesReturnsCardsWithIndexes() {
        List<Integer> cardOrderIndexes = List.of(2, 1);

        OrderedDeck deck = new OrderedDeck(cardOrderIndexes);

        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.THREE));
        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.TWO));
    }

    @Test
    void givenAMultipleListOfIndexesRearrangesCardsBasedOnTheNumbers() {
        List<Integer> cardOrderIndexes = new ArrayList<>();
        for (int i = 51; i >= 0; i--) {
            cardOrderIndexes.add(i);
        }

        OrderedDeck deck = new OrderedDeck(cardOrderIndexes);
        for (int i = 0; i <= 50; i++) {
            deck.draw();
        }

        assertThat(deck.draw())
                .isEqualTo(new Card(Suit.HEARTS, Rank.ACE));
    }

    @Test
    void cardIndexOutOfRangeThrowsException() throws Exception {
        List<Integer> cardOrderIndexes = List.of(52);

        assertThatThrownBy(() -> new OrderedDeck(cardOrderIndexes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card index is out of range, must be within 0 to 51");
    }

    @Test
    void listWithDuplicateNumbersThrowsException() {
        List<Integer> cardOrderIndexes = List.of(1, 1);

        assertThatThrownBy(() -> new OrderedDeck(cardOrderIndexes))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Found duplicate card indexes");

        // other #AssertJ options (not as precise, though)
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(() -> new OrderedDeck(cardOrderIndexes))
//                .withMessage("Found duplicate card indexes");
//
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> new OrderedDeck(cardOrderIndexes))
//                .withMessage("Found duplicate card indexes");

    }

    @Test
    void listWithMoreThan52NumbersThrowsException() {
        List<Integer> cardOrderIndexes = IntStream.range(0, 53).boxed().toList();

        assertThatThrownBy(() -> new OrderedDeck(cardOrderIndexes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Too many card indexes");
    }
}