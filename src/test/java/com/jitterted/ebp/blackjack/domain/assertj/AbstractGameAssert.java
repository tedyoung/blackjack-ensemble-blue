package com.jitterted.ebp.blackjack.domain.assertj;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.PlayerResult;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Objects;

/**
 * Abstract base class for {@link Game} specific assertions - Generated by CustomAssertionGenerator.
 */
@jakarta.annotation.Generated(value = "assertj-assertions-generator")
public abstract class AbstractGameAssert<S extends AbstractGameAssert<S, A>, A extends Game> extends AbstractObjectAssert<S, A> {

    /**
     * Creates a new <code>{@link AbstractGameAssert}</code> to make assertions on actual Game.
     *
     * @param actual the Game we want to make assertions on.
     */
    protected AbstractGameAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual Game's dealerHand is equal to the given one.
     *
     * @param dealerHand the given dealerHand to compare the actual Game's dealerHand to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Game's dealerHand is not equal to the given one.
     */
    public S hasDealerHand(Hand dealerHand) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // overrides the default error message with a more explicit one
        String assertjErrorMessage = "\nExpecting dealerHand of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

        // null safe check
        Hand actualDealerHand = actual.dealerHand();
        if (!Objects.areEqual(actualDealerHand, dealerHand)) {
            failWithMessage(assertjErrorMessage, actual, dealerHand, actualDealerHand);
        }

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game is game over.
     *
     * @return this assertion object.
     * @throws AssertionError - if the actual Game is not game over.
     */
    public S isGameOver() {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that property call/field access is true
        if (!actual.isGameOver()) {
            failWithMessage("\nExpecting that actual Game is game over but is not.");
        }

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game is not game over.
     *
     * @return this assertion object.
     * @throws AssertionError - if the actual Game is game over.
     */
    public S isNotGameOver() {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that property call/field access is false
        if (actual.isGameOver()) {
            failWithMessage("\nExpecting that actual Game is not game over but is.");
        }

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game's playerResults contains the given PlayerResult elements.
     *
     * @param playerResults the given elements that should be contained in actual Game's playerResults.
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults does not contain all given PlayerResult elements.
     */
    public S hasPlayerResults(PlayerResult... playerResults) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that given PlayerResult varargs is not null.
        if (playerResults == null) failWithMessage("Expecting playerResults parameter not to be null.");

        // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
        Iterables.instance().assertContains(info, actual.playerResults(), playerResults);

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game's playerResults contains the given PlayerResult elements in Collection.
     *
     * @param playerResults the given elements that should be contained in actual Game's playerResults.
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults does not contain all given PlayerResult elements.
     */
    public S hasPlayerResults(java.util.Collection<? extends PlayerResult> playerResults) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that given PlayerResult collection is not null.
        if (playerResults == null) {
            failWithMessage("Expecting playerResults parameter not to be null.");
            return myself; // to fool Eclipse "Null pointer access" warning on toArray.
        }

        // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
        Iterables.instance().assertContains(info, actual.playerResults(), playerResults.toArray());

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game's playerResults contains <b>only</b> the given PlayerResult elements and nothing else in whatever order.
     *
     * @param playerResults the given elements that should be contained in actual Game's playerResults.
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults does not contain all given PlayerResult elements.
     */
    public S hasOnlyPlayerResults(PlayerResult... playerResults) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that given PlayerResult varargs is not null.
        if (playerResults == null) failWithMessage("Expecting playerResults parameter not to be null.");

        // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
        Iterables.instance().assertContainsOnly(info, actual.playerResults(), playerResults);

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game's playerResults contains <b>only</b> the given PlayerResult elements in Collection and nothing else in whatever order.
     *
     * @param playerResults the given elements that should be contained in actual Game's playerResults.
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults does not contain all given PlayerResult elements.
     */
    public S hasOnlyPlayerResults(java.util.Collection<? extends PlayerResult> playerResults) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that given PlayerResult collection is not null.
        if (playerResults == null) {
            failWithMessage("Expecting playerResults parameter not to be null.");
            return myself; // to fool Eclipse "Null pointer access" warning on toArray.
        }

        // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
        Iterables.instance().assertContainsOnly(info, actual.playerResults(), playerResults.toArray());

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game's playerResults does not contain the given PlayerResult elements.
     *
     * @param playerResults the given elements that should not be in actual Game's playerResults.
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults contains any given PlayerResult elements.
     */
    public S doesNotHavePlayerResults(PlayerResult... playerResults) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that given PlayerResult varargs is not null.
        if (playerResults == null) failWithMessage("Expecting playerResults parameter not to be null.");

        // check with standard error message (use overridingErrorMessage before contains to set your own message).
        Iterables.instance().assertDoesNotContain(info, actual.playerResults(), playerResults);

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game's playerResults does not contain the given PlayerResult elements in Collection.
     *
     * @param playerResults the given elements that should not be in actual Game's playerResults.
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults contains any given PlayerResult elements.
     */
    public S doesNotHavePlayerResults(java.util.Collection<? extends PlayerResult> playerResults) {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // check that given PlayerResult collection is not null.
        if (playerResults == null) {
            failWithMessage("Expecting playerResults parameter not to be null.");
            return myself; // to fool Eclipse "Null pointer access" warning on toArray.
        }

        // check with standard error message (use overridingErrorMessage before contains to set your own message).
        Iterables.instance().assertDoesNotContain(info, actual.playerResults(), playerResults.toArray());

        // return the current assertion for method chaining
        return myself;
    }

    /**
     * Verifies that the actual Game has no playerResults.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual Game's playerResults is not empty.
     */
    public S hasNoPlayerResults() {
        // check that actual Game we want to make assertions on is not null.
        isNotNull();

        // we override the default error message with a more explicit one
        String assertjErrorMessage = "\nExpecting :\n  <%s>\nnot to have playerResults but had :\n  <%s>";

        // check
        if (actual.playerResults().iterator().hasNext()) {
            failWithMessage(assertjErrorMessage, actual, actual.playerResults());
        }

        // return the current assertion for method chaining
        return myself;
    }

    public S currentPlayerCardsAllFaceUp() {
        boolean isAnyCardFaceDown = AbstractHandAssert.isAnyCardFaceDown(actual.currentPlayerCards());

        String assertjErrorMessage = "\nExpecting current player cards in:\n  <%s>\nall the cards face up, but at least one was face down :\n  <%s>";
        if (isAnyCardFaceDown) {
            failWithMessage(assertjErrorMessage, actual, actual.currentPlayerCards());
        }

        return myself;
    }

    public AbstractHandAssert<HandAssert, Hand> currentPlayerHand() {
        return new HandAssert(new Hand(actual.currentPlayerCards()));
    }

}
