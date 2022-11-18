package com.jitterted.ebp.blackjack.domain.assertj;

import com.jitterted.ebp.blackjack.domain.Game;

/**
 * {@link Game} specific assertions - Generated by CustomAssertionGenerator.
 * <p>
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractGameAssert} instead.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class GameAssert extends AbstractGameAssert<GameAssert, Game> {

    /**
     * Creates a new <code>{@link GameAssert}</code> to make assertions on actual Game.
     *
     * @param actual the Game we want to make assertions on.
     */
    public GameAssert(Game actual) {
        super(actual, GameAssert.class);
    }

    /**
     * An entry point for GameAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
     * With a static import, one can write directly: <code>assertThat(myGame)</code> and get specific assertion with code completion.
     *
     * @param actual the Game we want to make assertions on.
     * @return a new <code>{@link GameAssert}</code>
     */
    @org.assertj.core.util.CheckReturnValue
    public static GameAssert assertThat(Game actual) {
        return new GameAssert(actual);
    }
}
