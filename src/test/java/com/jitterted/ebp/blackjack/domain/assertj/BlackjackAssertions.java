package com.jitterted.ebp.blackjack.domain.assertj;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class BlackjackAssertions {

  /**
   * Creates a new instance of <code>{@link CardAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static CardAssert assertThat(com.jitterted.ebp.blackjack.domain.Card actual) {
    return new CardAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link GameAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static GameAssert assertThat(com.jitterted.ebp.blackjack.domain.Game actual) {
    return new GameAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link HandAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static HandAssert assertThat(com.jitterted.ebp.blackjack.domain.Hand actual) {
    return new HandAssert(actual);
  }

  /**
   * Creates a new <code>{@link BlackjackAssertions}</code>.
   */
  protected BlackjackAssertions() {
    // empty
  }
}
